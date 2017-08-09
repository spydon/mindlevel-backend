package net.mindlevel.routes

import java.io.File
import java.nio.file.Paths
import java.time.Instant

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

import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object UserRoute extends AbstractRoute {
  def route: Route =
    pathPrefix("user") {
      pathEndOrSingleSlash {
        post {
          entity(as[LoginFormat]) { login =>
            val processedUser =
              UserRow(username = login.username, password = login.password.get.bcrypt, score = 0, created = now)
            val maybeInserted = db.run(User += processedUser)
            onSuccess(maybeInserted) {
              case 1 =>
                db.run(Session += SessionRow(username = login.username, session = None))
                complete(StatusCodes.OK)
              case _ =>
                complete(StatusCodes.BadRequest)
            }
          }
        }
      } ~
        pathPrefix(Segment) { username =>
          pathEndOrSingleSlash {
            get {
              val maybeUser = db.run(User.filter(_.username === username).result.headOption)
              //val userQuery =
              //  for {
              //    ((user, accomplishment), like) <-
              //    User join UserAccomplishment on (_.username === _.username) join AccomplishmentLike on (_._2.accomplishmentId === _.accomplishmentId)
              //    if user.username === username }
              //    yield (user.username, user.image, user.description, user.lastActive, user.created, like.score)

              //val q = for {
              //  ((username), ts) <- userQuery.groupBy(_.1))
              //} yield (username, ts.map(_._6).sum.getOrElse(0))

              //val userQuery = scoreQuery.map(_).sum()
              //val full =
              //  for {
              //    (u, s) <- userQuery zip scoreQuery
              //  } yield (u._1, s._1)
              //val run = db.run(q.result)

              onSuccess(maybeUser) {
                case Some(user) => complete(user.copy(password = ""))
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
                            .map(_ => "image" -> S3Util.put(file))

                        case b: BodyPart =>
                          b.toStrict(2.seconds).map(strict => b.name -> strict.entity.data.utf8String)

                      }.runFold(Map.empty[String, String])((map, tuple) => map + tuple)

                      val row = allParts.flatMap { parts =>
                        Unmarshal(parts("user")).to[UserRow].map {
                          _.copy(image = parts.get("image"))
                        }
                      }

                      val isUpdated = row.flatMap { userRow =>
                        isAuthorized(userRow.username, session) flatMap {
                          case true =>
                            val actions = mutable.ArrayBuffer[DBIOAction[Int, NoStream, Write with Transactional]]()
                            val query = User.withFilter(_.username === userRow.username)

                            if (!userRow.password.isEmpty) {
                              actions += query.map(_.password).update(userRow.password.bcrypt)
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

                onSuccess(accomplishments)(complete(_))
              }
            }
        }
    }
}
