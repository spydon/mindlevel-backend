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
import net.mindlevel.{S3Util, TimeUtil}
import slick.dbio.Effect.{Transactional, Write}

import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

import TimeUtil._

object UserRoute extends AbstractRoute {
  def route: Route =
    pathPrefix("user") {
      database { db =>
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
                val processedUser = UserRow(username = login.username, score = Some(0), level = Some(0), created = now)
                val emptySession = SessionRow(username = login.username, session = null)

                actions += (User += processedUser)
                actions += (UserExtra += userExtra)
                actions += (Session += emptySession)

                val maybeUpdated = db.run(DBIO.seq(actions.map(_.transactionally): _*))

                onSuccess(maybeUpdated)(complete(StatusCodes.OK))
              }
            }
        } ~
          path("usernames") {
            // Deprecated, old versions still rely on this, use stats route instead
            get {
              pathPrefix("highscore") {
                val usernames = db.run(User.map(_.username).result)
                onSuccess(usernames)(complete(_))
              }
            }
          } ~
          pathPrefix("highscore") {
            // Deprecated, old versions still rely on this, use stats route instead
            path(IntNumber) { amount =>
              get {
                val users = db.run(User.sortBy(_.score.desc).take(amount).result)
                onSuccess(users)(complete(_))
              }
            }
          } ~
          pathPrefix(Segment) { username =>
            pathEndOrSingleSlash {
              sessionId() { session =>
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
                          isAuthorized(db, userRow.username, session) flatMap {
                            case true =>

                              val actions = mutable.ArrayBuffer[DBIOAction[Int, NoStream, Write with Transactional]]()
                              val query = User.withFilter(_.username === userRow.username)
                              val extraQuery = UserExtra.withFilter(_.username === userRow.username)

                              extraRow.map { userExtraRow =>
                                // Fire and forget ue to that this field might not exist
                                val extraActions = mutable.ArrayBuffer[DBIOAction[Int, NoStream, Write with Transactional]]()
                                if (!userExtraRow.password.isEmpty) {
                                  extraActions += extraQuery.map(_.password).update(userExtraRow.password.bcrypt)
                                }
                                if (!userExtraRow.email.isEmpty) {
                                  extraActions += extraQuery.map(_.email).update(userExtraRow.email)
                                }
                                db.run(DBIO.seq(extraActions.map(_.transactionally): _*))
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
                    } ~
                      entity(as[LoginFormat]) { user =>
                        onSuccess(updatePassword(db, user)) { session =>
                          complete(session)
                        }
                      }
                  }
              } ~
                path("email") {
                  sessionId() { session =>
                    get {
                      onSuccess(isAuthorized(db, username, session)) {
                        case true =>
                          val query = db.run(UserExtra.filter(_.username === username).map(_.email).result.headOption)
                          onSuccess(query)(complete(_))
                        case false =>
                          complete(StatusCodes.Unauthorized)
                      }
                    }
                  }
                } ~
                path("image") {
                  sessionId() { session =>
                    post {
                      onSuccess(isAuthorized(db, username, session)) {
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
                    val accomplishments = db.run(UserAccomplishment.filter(_.username === username).flatMap(ua =>
                      Accomplishment.filter(_.id === ua.accomplishmentId)
                    ).sortBy(_.created.desc).result)
                    complete(accomplishments)
                  }
                } ~
                pathPrefix("notification") {
                  sessionId(false) { session =>
                    pathEndOrSingleSlash {
                      get {
                        val innerJoin = for {
                          (_, n) <-
                            NotificationUser.filter(nu => nu.username === username && !nu.seen) join Notification on (_.notificationId === _.id)
                        } yield n

                        onSuccess(db.run(innerJoin.result)) { notifications =>
                          complete(notifications)
                        }
                      }
                    } ~
                      path(IntNumber) { notificationId =>
                        delete {
                          onSuccess(isAuthorized(db, username, session)) {
                            case true =>
                              val q = for {
                                nu <- NotificationUser if nu.username === username && nu.notificationId === notificationId
                              } yield nu.seen
                              val maybeUpdated = db.run(q.update(true))
                              onSuccess(maybeUpdated) {
                                case 0 => complete(StatusCodes.NotFound)
                                case _ => complete(StatusCodes.OK)
                              }
                            case false =>
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
}
