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

object MissionRoute extends AbstractRoute {
  def route: Route =
    pathPrefix("mission") {
      pathEndOrSingleSlash {
        get {
          complete(db.run(Mission.result))
        } ~
          post {
            entity(as[MissionRow]) { mission =>
              val nonValidatedMission = mission.copy(validated = false)
              val maybeInserted = db.run(Mission += nonValidatedMission)
              onSuccess(maybeInserted) {
                case 1 => complete(StatusCodes.OK)
                case _ => complete(StatusCodes.BadRequest)
              }
            }
          }
      } ~
        path(IntNumber) { id =>
          pathEndOrSingleSlash {
            get {
              val maybeMission = db.run(Mission.filter(_.id === id).result.headOption)

              onSuccess(maybeMission) {
                case Some(mission) => complete(mission)
                case None => complete(StatusCodes.NotFound)
              }
            }
          } ~
            path("image") {
              post {
                headerValueByName("X-Session") { session =>
                  onSuccess(isAuthorizedToMission(id, session)) {
                    case true =>
                      extractRequestContext { ctx =>
                        implicit val materializer = ctx.materializer
                        implicit val ec = ctx.executionContext
                        fileUpload("image") {
                          case (fileInfo, fileStream) =>
                            val sink = FileIO.toPath(Paths.get("/tmp") resolve fileInfo.fileName)
                            val writeResult = fileStream.runWith(sink)
                            onSuccess(writeResult) { result =>
                              result.status match {
                                case Success(_) => complete(s"Successfully written ${result.count} bytes")
                                case Failure(e) => throw e
                              }
                            }
                        }
                      }
                    case false =>
                      complete(StatusCodes.Unauthorized)
                  }
                }
              }
            }
        } ~
        path(Segment) { range =>
          get {
            if (range.contains("-")) {
              val between = range.split("-")
              val lower = between(0).toInt
              val upper = between(1).toInt
              val missions = db.run(Mission.filter(_.id >= lower).filter(_.id <= upper).result)
              complete(missions)
            } else if (range.contains(",")) {
              val ids = range.split(",").map(_.toInt)
              val query = for {
                m <- Mission if m.id inSetBind ids
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
