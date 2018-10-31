package net.mindlevel.routes

import java.nio.file.Paths

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.FileIO
import net.mindlevel.models.Tables._
import slick.jdbc.MySQLProfile.api._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object ChallengeRoute extends AbstractRoute {
  def removeRestricted(session: String)(challenges: Query[Challenge, Challenge#TableElementType, Seq]):
  Future[Seq[Challenge#TableElementType]] = {
    userFromSession(session).map { user =>
      challenges.filter(_.scoreRestriction <= user.score).filter(_.levelRestriction <= user.level)
    }.flatMap(q => db.run(q.result))
  }

  def route: Route =
    pathPrefix("challenge") {
      headerValueByName("X-Session") { session =>
        def clean = removeRestricted(session)(_)

        pathEndOrSingleSlash {
          get {
            complete(clean(Challenge))
          } ~
            post {
              entity(as[ChallengeRow]) { challenge =>
                val nonValidatedChallenge = challenge.copy(validated = false)
                val maybeInserted = db.run(Challenge += nonValidatedChallenge)
                onSuccess(maybeInserted) {
                  case 1 => complete(StatusCodes.OK)
                  case _ => complete(StatusCodes.BadRequest)
                }
              }
            }
        } ~
          pathPrefix("latest") {
            pathEndOrSingleSlash {
              get {
                val challenges = clean(Challenge.sortBy(_.created.desc).take(challengePageSize))
                complete(challenges)
              }
            } ~
              path(IntNumber) { pageSize =>
                get {
                  val challenges = clean(Challenge.sortBy(_.created.desc).take(pageSize))
                  complete(challenges)
                }
              } ~
              path(Segment) { range =>
                get {
                  if (range.contains("-")) {
                    val between = range.split("-")
                    val drop = between(0).toInt - 1
                    val upper = between(1).toInt
                    val take = upper - drop
                    if (drop < upper) {
                      val challenges = clean(Challenge.sortBy(_.created.desc).drop(drop).take(take))
                      complete(challenges)
                    } else {
                      complete(StatusCodes.BadRequest)
                    }
                  } else {
                    complete(StatusCodes.BadRequest)
                  }
                }
              }
          } ~
          path("restricted") {
            get {
              // TODO: Make this not return name and description of challenge
              complete(db.run(Challenge.result))
            }
          } ~
          pathPrefix("category") {
            pathEndOrSingleSlash {
              get {
                complete(db.run(Category.result))
              }
            } ~
              path(IntNumber) { categoryId =>
                get {
                  val challenges = db.run(ChallengeCategory.filter(_.categoryId === categoryId).flatMap(cc =>
                    Challenge.filter(_.id === cc.challengeId)
                  ).result)
                  complete(challenges)
                }
              }
          } ~
          pathPrefix(IntNumber) { id =>
            pathEndOrSingleSlash {
              get {
                val maybeChallenge = clean(Challenge.filter(_.id === id)).map(_.headOption)
                complete(maybeChallenge)
              }
            } ~
              path("image") {
                post {
                  onSuccess(isAuthorizedToChallenge(id, session)) {
                    case true =>
                      extractRequestContext { ctx =>
                        implicit val materializer = ctx.materializer
                        implicit val ec = ctx.executionContext
                        fileUpload("image") {
                          case (fileInfo, fileStream) =>
                            val filename = fileInfo.fileName // TODO: Hash later
                            val sink = FileIO.toPath(Paths.get("/tmp") resolve filename)
                            val writeResult = fileStream.runWith(sink)
                            onSuccess(writeResult) { result =>
                              result.status match {
                                case Success(_) =>
                                  val q = for {m <- Challenge if m.id === id} yield m.image
                                  db.run(q.update(filename)) // Fire and forget
                                  complete(s"Successfully written ${result.count} bytes")
                                case Failure(e) =>
                                  throw e
                              }
                            }
                        }
                      }
                    case false =>
                      complete(StatusCodes.Unauthorized)
                  }
                }
              } ~
              path("accomplishment") {
                get {
                  val query = Accomplishment.filter(_.challengeId === id)
                  complete(AccomplishmentRoute.removeRestricted(session)(query))
                }
              }
          } ~
          path(Segment) { range =>
            get {
              if (range.contains("-")) {
                val between = range.split("-")
                val lower = between(0).toInt
                val upper = between(1).toInt
                val challenges = clean(Challenge.filter(_.id >= lower).filter(_.id <= upper))
                complete(challenges)
              } else if (range.contains(",")) {
                val ids = range.split(",").map(_.toInt)
                val query = for {
                  m <- Challenge if m.id inSetBind ids
                } yield m
                val challenges = clean(query)
                complete(challenges)
              } else {
                complete(StatusCodes.BadRequest)
              }
            }
          }
      }
    }
}
