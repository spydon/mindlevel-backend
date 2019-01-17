package net.mindlevel.routes

import java.io.File
import java.nio.file.Paths
import java.sql.Timestamp

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.Multipart.FormData.BodyPart
import akka.http.scaladsl.model.{Multipart, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshal

import scala.concurrent.duration._
import akka.stream.scaladsl.FileIO
import net.mindlevel.S3Util
import net.mindlevel.models.Tables._
import slick.jdbc.MySQLProfile.api._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object ChallengeRoute extends AbstractRoute {
  def removeRestricted(db: Database, session: String)(challenges: Query[Challenge, Challenge#TableElementType, Seq]):
  Future[Seq[Challenge#TableElementType]] = {
    userFromSession(db, session).map { user =>
      challenges
        .filter(_.scoreRestriction <= user.score)
        .filter(_.levelRestriction <= user.level)
        .filter(_.validated)
    }.flatMap(q => db.run(q.result))
  }

  def route: Route =
    pathPrefix("challenge") {
      database { db =>
        sessionId { session =>
          def clean = removeRestricted(db, session)(_)

          pathEndOrSingleSlash {
            get {
              complete(clean(Challenge))
            } ~
              post {
                extractRequestContext { ctx =>
                  implicit val materializer = ctx.materializer
                  implicit val ec = ctx.executionContext
                  entity(as[Multipart.FormData]) { formData =>
                    onSuccess(nameFromSession(db, session)) {
                      case Some(username) =>
                        // collect all parts of the multipart as it arrives into a map
                        val allParts: Future[Map[String, String]] = formData.parts.mapAsync[(String, String)](1) {

                          case b: BodyPart if b.name == "image" =>
                            val file = File.createTempFile("upload", "tmp")
                            b.entity.dataBytes.runWith(FileIO.toPath(file.toPath)).map(_ => "image" -> S3Util.put(file))

                          case b: BodyPart =>
                            b.toStrict(2.seconds).map(strict => b.name -> strict.entity.data.utf8String)

                        }.runFold(Map.empty[String, String])((map, tuple) => map + tuple)

                        // TODO: Might want restriction on whether client can set level restriction in the request or if it
                        // should take the level restriction of the challenge
                        val row = allParts.flatMap { parts =>
                          Unmarshal(parts("challenge")).to[ChallengeRow].map { challengeRow =>
                            challengeRow.copy(
                              image = parts("image"),
                              created = new Timestamp(now()),
                              creator = username,
                              validated = false,
                              levelRestriction = None,
                              scoreRestriction = None
                            )
                          }
                        }

                        onSuccess(row) { challenge =>
                          onSuccess(db.run(Challenge += challenge)) { result =>
                            if (result > 0) {
                              complete(StatusCodes.OK)
                            } else {
                              complete(StatusCodes.InternalServerError)
                            }
                          }
                        }
                      case _ =>
                        complete(StatusCodes.Unauthorized)
                    }
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
                complete(db.run(Challenge.filter(_.validated).result))
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
                    val challenges = clean(
                      ChallengeCategory
                        .filter(_.categoryId === categoryId)
                        .flatMap(cc =>
                          Challenge.filter(_.id === cc.challengeId)
                        )
                    )
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
                    onSuccess(isAuthorizedToChallenge(db, id, session)) {
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
                    val query = Accomplishment.filter(_.challengeId === id).sortBy(_.created.desc)
                    complete(AccomplishmentRoute.removeRestricted(db, session)(query))
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
}
