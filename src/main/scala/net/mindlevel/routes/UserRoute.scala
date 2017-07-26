package net.mindlevel.routes

import java.nio.file.Paths

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.stream.scaladsl.FileIO
import spray.json.DefaultJsonProtocol._
import slick.jdbc.MySQLProfile.api._
import net.mindlevel.models.Tables._
import com.github.t3hnar.bcrypt._
import scala.util.{Failure, Success}

object UserRoute extends AbstractRoute {
  def route: Route =
    pathPrefix("user") {
      pathEndOrSingleSlash {
        post {
          entity(as[UserRow]) { user =>
            val processedUser = user.copy(password = user.password.bcrypt)
            val maybeInserted = db.run(User += processedUser)
            onSuccess(maybeInserted) {
              case 1 => complete(StatusCodes.OK)
              case _ => complete(StatusCodes.BadRequest)
            }
          }
        }
      } ~
        pathPrefix(Segment) { username =>
          pathEndOrSingleSlash {
            get {
              val maybeUser = db.run(User.filter(_.username === username).result.headOption)

              onSuccess(maybeUser) {
                case Some(user) => complete(user.copy(password = ""))
                case None => complete(StatusCodes.NotFound)
              }
            } ~
              put {
                entity(as[UserRow]) { user =>
                  headerValueByName("X-Session") { session =>
                    onSuccess(isAuthorized(user.username, session)) {
                      case true =>
                        val q = for {u <- User if u.username === user.username} yield (u.description, u.image)
                        val maybeUpdated = db.run(q.update(user.description, user.image))

                        onSuccess(maybeUpdated) {
                          case 1 => complete(StatusCodes.OK)
                          case _ => complete(StatusCodes.BadRequest)
                        }
                      case false =>
                        complete(StatusCodes.Unauthorized)
                    }
                  }
                } ~
                entity(as[LoginFormat]) { user =>
                  onSuccess(updatePassword(user)) { session =>
                    complete(session)
                  }
                }
              }
          } ~
            path("image") {
              post {
                headerValueByName("X-Session") { session =>
                  onSuccess(isAuthorized(username, session)) {
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
                                  val q = for {u <- User if u.username === username} yield u.image
                                  db.run(q.update(Some(filename))) // Fire and forget
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
                val accomplishments = db.run(UserAccomplishment.filter(_.username === username).flatMap( ua =>
                  Accomplishment.filter(_.id === ua.accomplishmentId)
                ).result)

                onSuccess(accomplishments)(complete(_))
              }
            }
        }
    }
}
