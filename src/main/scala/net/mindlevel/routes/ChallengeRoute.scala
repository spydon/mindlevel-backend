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
import scala.util.{Failure, Success}

object ChallengeRoute extends AbstractRoute {
  def route: Route =
    pathPrefix("challenge") {
      pathEndOrSingleSlash {
        get {
          complete(db.run(Challenge.result))
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
      }  ~
        pathPrefix("latest") {
          pathEndOrSingleSlash {
            get {
              val missions = db.run(Challenge.sortBy(_.created.desc).take(missionPageSize).result)
              complete(missions)
            }
          } ~
            path(IntNumber) { pageSize =>
              get {
                val missions = db.run(Challenge.sortBy(_.created.desc).take(pageSize).result)
                complete(missions)
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
                    val missions = db.run(Challenge.sortBy(_.created.desc).drop(drop).take(take).result)
                    complete(missions)
                  } else {
                    complete(StatusCodes.BadRequest)
                  }
                } else {
                  complete(StatusCodes.BadRequest)
                }
              }
            }
        } ~
        pathPrefix(IntNumber) { id =>
          pathEndOrSingleSlash {
            get {
              val maybeChallenge = db.run(Challenge.filter(_.id === id).result.headOption)

              onSuccess(maybeChallenge) {
                case Some(challenge) => complete(challenge)
                case None => complete(StatusCodes.NotFound)
              }
            }
          } ~
            path("image") {
              post {
                headerValueByName("X-Session") { session =>
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
              }
            } ~
            path("accomplishment") {
              get {
                complete(db.run(Accomplishment.filter(_.missionId === id).result))
              }
            }
        } ~
        path(Segment) { range =>
          get {
            if (range.contains("-")) {
              val between = range.split("-")
              val lower = between(0).toInt
              val upper = between(1).toInt
              val missions = db.run(Challenge.filter(_.id >= lower).filter(_.id <= upper).result)
              complete(missions)
            } else if (range.contains(",")) {
              val ids = range.split(",").map(_.toInt)
              val query = for {
                m <- Challenge if m.id inSetBind ids
              } yield m
              val missions = db.run(query.result)
              complete(missions)
            } else {
              complete(StatusCodes.BadRequest)
            }
          }
        }
    }
}
