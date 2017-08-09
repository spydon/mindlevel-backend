package net.mindlevel.routes

import java.io.File
import java.nio.file.Paths

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.Multipart.FormData.BodyPart
import akka.http.scaladsl.model.{ContentType, Multipart, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.scaladsl.FileIO
import net.mindlevel.S3Util
import net.mindlevel.models.Tables._
import slick.jdbc.MySQLProfile.api._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object AccomplishmentRoute extends AbstractRoute {

  def route: Route =
    pathPrefix("accomplishment") {
      pathEndOrSingleSlash {
        post {
          extractRequestContext { ctx =>
            implicit val materializer = ctx.materializer
            implicit val ec = ctx.executionContext
            entity(as[Multipart.FormData]) { formData =>
              headerValueByName("X-Session") { session =>
                onSuccess(nameFromSession(session)) {
                  case Some(username) =>
                    // collect all parts of the multipart as it arrives into a map
                    val allParts: Future[Map[String, String]] = formData.parts.mapAsync[(String, String)](1) {

                      case b: BodyPart if b.name == "image" =>
                        val file = File.createTempFile("upload", "tmp")
                        b.entity.dataBytes.runWith(FileIO.toPath(file.toPath)).map(_ => "image" -> S3Util.put(file))

                      case b: BodyPart =>
                        b.toStrict(2.seconds).map(strict => b.name -> strict.entity.data.utf8String)

                    }.runFold(Map.empty[String, String])((map, tuple) => map + tuple)

                    val row = allParts.flatMap { parts =>
                      Unmarshal(parts("accomplishment")).to[AccomplishmentRow].map {
                        _.copy(image = parts("image"), score = 0, created = Some(now))
                      }
                    }

                    onSuccess(row) { accomplishment =>
                      val accomplishmentWithIdQuery =
                        (Accomplishment returning Accomplishment.map(_.id) into ((accomplishment: AccomplishmentRow, id) =>
                          accomplishment.copy(id = id)) += accomplishment)
                      onSuccess(db.run(accomplishmentWithIdQuery)) { accomplishment =>
                        val userAccomplishmentRow = UserAccomplishmentRow(username, accomplishment.id)
                        onSuccess(db.run(UserAccomplishment += userAccomplishmentRow)) {
                          case 1 => complete(accomplishment)
                          case _ => complete(StatusCodes.BadRequest)
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
      } ~
        pathPrefix("latest") {
          pathEndOrSingleSlash {
            get {
              val accomplishments = db.run(Accomplishment.sortBy(_.created.desc).take(accomplishmentPageSize).result)
              complete(accomplishments)
            }
          } ~
            path(IntNumber) { pageSize =>
              get {
                val accomplishments = db.run(Accomplishment.sortBy(_.created.desc).take(pageSize).result)
                complete(accomplishments)
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
                    val accomplishments = db.run(Accomplishment.sortBy(_.created.desc).drop(drop).take(take).result)
                    complete(accomplishments)
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
          val maybeAccomplishment = db.run(Accomplishment.filter(_.id === id).result.headOption)
          pathEndOrSingleSlash {
            get {
              onSuccess(maybeAccomplishment) {
                case Some(accomplishment) => complete(accomplishment)
                case None => complete(StatusCodes.NotFound)
              }
            }
          } ~
            path("like") {
              get {
                headerValueByName("X-Session") { session =>
                  onSuccess(nameFromSession(session)) {
                    case Some(username) =>
                      val scoreValue = 1
                      val accomplishmentLike =
                        AccomplishmentLikeRow(username = username, accomplishmentId = id, score = scoreValue)
                      val maybeInserted = db.run(AccomplishmentLike += accomplishmentLike)

                      onSuccess(maybeInserted) {
                        case 1 =>
                          // Give score to affected users
                          // Get all users from user_accomplishment join with user
                          // increment score for accomplishment and user
                          val updateScore =
                            sqlu"""UPDATE user u, accomplishment a
                              join user_accomplishment
                              join accomplishment on user_accomplishment.accomplishment_id = accomplishment.id
                              SET u.score = u.score + ${scoreValue}, a.score = a.score + ${scoreValue}
                              WHERE a.id = ${id}"""

                          db.run(updateScore) // Fire and forget
                          val maybeLikes = db.run(Accomplishment.filter(_.id === id).result.headOption)
                          onSuccess(maybeLikes) { likes =>
                            complete(likes.get.score.toString)
                          }
                        case _ =>
                          complete(StatusCodes.BadRequest)
                      }
                    case None =>
                      complete(StatusCodes.Unauthorized)
                  }
                }
              }
            } ~
            path("contributor") {
              get {
                onSuccess(maybeAccomplishment) {
                  case Some(accomplishment) =>
                    val contributors =
                      db.run(UserAccomplishment.filter(_.accomplishmentId === accomplishment.id).map(_.username).result)
                    onSuccess(contributors)(complete(_))
                  case None =>
                    complete(StatusCodes.NotFound)
                }
              } ~
                post {
                  entity(as[Seq[String]]) { usernames =>
                    onSuccess(maybeAccomplishment) {
                      case Some(_) =>
                        headerValueByName("X-Session") { session =>
                          onSuccess(isAuthorizedToAccomplishment(id, session)) {
                            case true =>
                              val userAccomplishmentRows = usernames.map(UserAccomplishmentRow(_, id))
                              val maybeInserted = db.run(UserAccomplishment ++= userAccomplishmentRows)
                              onSuccess(maybeInserted) {
                                case Some(_) => complete(StatusCodes.OK)
                                case None => complete(StatusCodes.BadRequest)
                              }
                            case false => complete(StatusCodes.Unauthorized)
                          }
                        }
                      case None => complete(StatusCodes.NotFound)
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
              val accomplishments = db.run(Accomplishment.filter(_.id >= lower).filter(_.id <= upper).result)
              complete(accomplishments)
            } else if (range.contains(",")) {
              val ids = range.split(",").map(_.toInt)
              val query = for {
                m <- Accomplishment if m.id inSetBind ids
              } yield m
              val accomplishments = db.run(query.result)
              complete(accomplishments)
            } else {
              complete(StatusCodes.BadRequest)
            }
          }
        }
    }
}
