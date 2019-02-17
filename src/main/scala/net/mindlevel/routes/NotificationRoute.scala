package net.mindlevel.routes

import java.io.File
import java.sql.Timestamp

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.Multipart.FormData.BodyPart
import akka.http.scaladsl.model.{Multipart, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.scaladsl.FileIO
import net.mindlevel.S3Util
import net.mindlevel.models.Tables._
import slick.jdbc.MySQLProfile.api._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

import net.mindlevel.TimeUtil._

object NotificationRoute extends AbstractRoute {

  private type NotificationQuery = Query[Notification, Notification#TableElementType, Seq]

  def route: Route =
    pathPrefix("notification") {
      database { db =>
        pathEndOrSingleSlash {
          post {
            sessionId() { session =>
              extractRequestContext { ctx =>
                implicit val materializer = ctx.materializer
                implicit val ec = ctx.executionContext
                entity(as[Multipart.FormData]) { formData =>
                  onSuccess(nameFromSession(db, session)) {
                    case Some("lukas") => // TODO: When this should be used, fix permissions
                      // collect all parts of the multipart as it arrives into a map
                      val allParts: Future[Map[String, String]] = formData.parts.mapAsync[(String, String)](1) {

                        case b: BodyPart if b.name == "image" =>
                          val file = File.createTempFile("upload", "tmp")
                          b.entity.dataBytes.runWith(FileIO.toPath(file.toPath)).map(_ => "image" -> S3Util.put(file))

                        case b: BodyPart =>
                          b.toStrict(2.seconds).map(strict => b.name -> strict.entity.data.utf8String)

                      }.runFold(Map.empty[String, String])((map, tuple) => map + tuple)

                      val row = allParts.flatMap { parts =>
                        Unmarshal(parts("notification")).to[NotificationRow].map { notificationRow =>
                          notificationRow.copy(
                            image = Some(parts("image")),
                            created = new Timestamp(now())
                          )
                        }
                      }

                      onSuccess(row) { notification =>
                        onSuccess(db.run(Notification += notification)) { result =>
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
          }
        }
      }
    }
}
