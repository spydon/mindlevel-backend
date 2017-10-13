package net.mindlevel.routes

import java.io.File
import java.nio.file.Paths

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{Multipart, StatusCodes}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.Multipart.FormData.BodyPart
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.scaladsl.FileIO
import spray.json.DefaultJsonProtocol._
import slick.jdbc.MySQLProfile.api._
import net.mindlevel.models.Tables._
import com.github.t3hnar.bcrypt._
import net.mindlevel.S3Util
import net.mindlevel.models.Tables
import slick.dbio.Effect.{Transactional, Write}
import slick.model.Table

import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.util.{Failure, Success}

object UserRoute extends AbstractRoute {
  def route: Route =
    pathPrefix("user") {
      pathEndOrSingleSlash {
        get {
          onSuccess(db.run(User.result)) {
            complete(_)
          }
        } ~
        post {
          entity(as[LoginFormat]) { login =>
            val actions = mutable.ArrayBuffer[DBIOAction[Int, NoStream, Write with Transactional]]()
            val userExtra = UserExtraRow(username = login.username, password = login.password.get.bcrypt, email = "")
            val processedUser = UserRow(username = login.username, score = 0, created = now)

            actions += (User += processedUser)
            actions += (UserExtra += userExtra)

            val maybeUpdated = db.run(DBIO.seq(actions.map(_.transactionally): _*))

            onSuccess(maybeUpdated)(complete(StatusCodes.OK))
          }
        }
      } ~
        path("usernames") {
          get {
            val usernames = db.run(User.map(_.username).result)
            onSuccess(usernames)(complete(_))
          }
        } ~
        pathPrefix("highscore") {
          path(IntNumber) { amount =>
            get {
              val users = db.run(User.sortBy(_.score.desc).take(amount).result)
              onSuccess(users)(complete(_))
            }
          }
        } ~
        pathPrefix(Segment) { username =>
          pathEndOrSingleSlash {
            get {
              val maybeUser = db.run(User.filter(_.username === username).result.headOption)

              onSuccess(maybeUser) {
                case Some(user) => complete(user)
                case None => complete(StatusCodes.NotFound)
              }
            } ~
              put {
                extractRequestContext { ctx =>
                  implicit val materializer = ctx.materializer
                  implicit val ec = ctx.executionContext
                  entity(as[Multipart.FormData]) { formData =>
                    headerValueByName("X-Session") { session =>
                      val allParts: Future[Map[String, String]] = formData.parts.mapAsync[(String, String)](1) {

                        case b: BodyPart if b.name == "image" =>
                          val file = File.createTempFile("upload", "tmp")
                          b.entity.dataBytes.runWith(FileIO.toPath(file.toPath))
                            .map(_ => "image" -> S3Util.put(file)) // TODO: Handle exception or map to akka-http error

                        case b: BodyPart =>
                          b.toStrict(2.seconds).map(strict => b.name -> strict.entity.data.utf8String)

                      }.runFold(Map.empty[String, String])((map, tuple) => map + tuple)

                      val row = allParts.flatMap { parts =>
                        Unmarshal(parts("user")).to[UserRow].map {
                          _.copy(image = parts.get("image"))
                        }
                      }

                      val extraRow = allParts.flatMap { parts => Unmarshal(parts("extra")).to[UserExtraRow] }

                      val isUpdated = row.flatMap { userRow =>
                        isAuthorized(userRow.username, session) flatMap {
                          case true =>
                            val actions = mutable.ArrayBuffer[DBIOAction[Int, NoStream, Write with Transactional]]()
                            val query = User.withFilter(_.username === userRow.username)
                            val extraQuery = UserExtra.withFilter(_.username === userRow.username)

                            extraRow.map { userExtraRow =>
                              if (!userExtraRow.password.isEmpty) {
                                actions += extraQuery.map(_.password).update(userExtraRow.password.bcrypt)
                              }
                              if (!userExtraRow.email.isEmpty) {
                                actions += extraQuery.map(_.email).update(userExtraRow.email)
                              }
                            }

                            if (userRow.description.isDefined) {
                              actions += query.map(_.description).update(userRow.description)
                            }

                            if (userRow.image.isDefined) {
                              actions += query.map(_.image).update(userRow.image)
                            }

                            val maybeUpdated = db.run(DBIO.seq(actions.map(_.transactionally): _*))

                            maybeUpdated map {
                              _ => complete(StatusCodes.OK)
                            }
                          case false =>
                            Future(complete(StatusCodes.Unauthorized))
                        }
                      }

                      onSuccess(isUpdated) { result => result } // TODO: Smarter extraction
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

                // TODO: Check if this is OK
                //onSuccess(accomplishments)(complete(_))
                complete(accomplishments)
              }
            }
        }
    }
}
