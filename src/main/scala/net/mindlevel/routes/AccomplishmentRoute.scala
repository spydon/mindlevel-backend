package net.mindlevel.routes

import java.io.File

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
import scala.util.Success

object AccomplishmentRoute extends AbstractRoute {

  private type AccomplishmentQuery = Query[Accomplishment, Accomplishment#TableElementType, Seq]

  def removeRestricted(session: String)(accomplishments: AccomplishmentQuery):
  Future[Seq[Accomplishment#TableElementType]] = {
    userFromSession(session).map { user =>
      accomplishments.filter(_.scoreRestriction <= user.score).filter(_.accomplishmentRestriction <= user.level)
    }.flatMap(q => db.run(q.result))
  }

  def route: Route =
    pathPrefix("accomplishment") {
      headerValueByName("X-Session") { session =>
        def clean = removeRestricted(session)(_)

        def cleanHead(query: AccomplishmentQuery) =
          removeRestricted(session)(query).map(_.headOption)

        pathEndOrSingleSlash {
          post {
            extractRequestContext { ctx =>
              implicit val materializer = ctx.materializer
              implicit val ec = ctx.executionContext
              entity(as[Multipart.FormData]) { formData =>
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
                        _.copy(image = parts("image"), score = 0, created = Some(now()))
                      }
                    }

                    val contributorsF = allParts.flatMap { parts =>
                      Unmarshal(parts("contributors")).to[Contributors].map(_.contributors)
                    }

                    onSuccess(row) { accomplishment =>
                      val accomplishmentWithIdQuery =
                        Accomplishment returning Accomplishment.map(_.id) into ((accomplishment: AccomplishmentRow, id) =>
                          accomplishment.copy(id = id)) += accomplishment
                      onSuccess(db.run(accomplishmentWithIdQuery)) { accomplishment =>
                        onSuccess(contributorsF) { contributors =>
                          val creatorAccomplishmentRow = UserAccomplishmentRow(username, accomplishment.id)
                          val contributorRows =
                            contributors.map { contributor =>
                              UserAccomplishmentRow(username = contributor, accomplishmentId = accomplishment.id)
                            }
                          val contributorSet = (creatorAccomplishmentRow :: contributorRows).toSet
                          onSuccess(db.run(UserAccomplishment ++= contributorSet)) { _ =>
                            val user = for { u <- User if u.username === username } yield u.level
                            // Take out all accomplishments for a user and count the unique ones
                            val levelQuery = (for {
                              (a, ua) <- Accomplishment join UserAccomplishment on (_.id === _.accomplishmentId)
                                         if ua.username === username
                            } yield a.challengeId).distinct.size
                            onSuccess(db.run(levelQuery.result)) { level =>
                              val userUpdate = db.run(user.update(Some(level)))
                              onSuccess(userUpdate) { _ =>
                                complete(accomplishment)
                              }
                            }

                          }
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
                val accomplishments = clean(Accomplishment.sortBy(_.created.desc).take(accomplishmentPageSize))
                complete(accomplishments)
              }
            } ~
              path(IntNumber) { pageSize =>
                get {
                  val accomplishments = clean(Accomplishment.sortBy(_.created.desc).take(pageSize))
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
                      val accomplishments = clean(Accomplishment.sortBy(_.created.desc).drop(drop).take(take))
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
            val maybeAccomplishment = cleanHead(Accomplishment.filter(_.id === id))
            pathEndOrSingleSlash {
              get {
                complete(maybeAccomplishment)
              }
            } ~
              path("like") {
                get {
                  onSuccess(nameFromSession(session)) {
                    case None =>
                      complete(StatusCodes.Unauthorized)
                    case Some(username) =>
                      val scoreValue = 1
                      val accomplishmentLike =
                        AccomplishmentLikeRow(
                          username = username, accomplishmentId = id, score = scoreValue, created = now())
                      val maybeAllowed = db.run(UserAccomplishment.filter(_.username === username).result.headOption)
                      onSuccess(maybeAllowed) { accomplishments: Option[UserAccomplishment#TableElementType] =>
                        // This makes sure that the user has a finished accomplishment before registering like
                        val maybeInserted = accomplishments match {
                          case None => Future.failed(new Exception("User does not have an accomplishment yet"))
                          case _ => db.run(AccomplishmentLike += accomplishmentLike)
                        }

                        def scoreResponse(first: Boolean) = {
                          val maybeLikes = db.run(Accomplishment.filter(_.id === id).result.headOption)
                          onSuccess(maybeLikes) {
                            case Some(likes) => complete(LikeResponse(first, likes.score.toString))
                            case None => complete(StatusCodes.NotFound)
                          }
                        }

                        onComplete(maybeInserted) {
                          case Success(_) =>
                            // Give score to affected users
                            // Get all users from user_accomplishment join with user
                            // increment score for accomplishment and user
                            val updateScore =
                              sqlu"""UPDATE accomplishment a
                              JOIN user_accomplishment ua ON ua.accomplishment_id = a.id
                              JOIN user u ON u.username = ua.username
                              SET u.score = u.score + $scoreValue, a.score = a.score + $scoreValue
                              WHERE a.id = $id"""

                            // Fire and forget, significant race? (Can be recounted from accomplishment_like)
                            db.run(updateScore)
                            scoreResponse(true)
                          case _ =>
                            scoreResponse(false) // Already liked or user does not have 1 accomplishment yet
                        }
                      }
                  }
                }
              } ~
              path("contributor") {
                get {
                  val innerJoin = for {
                    (_, u) <-
                    UserAccomplishment.filter(_.accomplishmentId === id) join User on (_.username === _.username)
                  } yield u

                  onSuccess(db.run(innerJoin.result)) { users =>
                    complete(users)
                  }
                } ~
                  post {
                    entity(as[Seq[String]]) { usernames =>
                      onSuccess(isAuthorizedToAccomplishment(id, session)) {
                        case true =>
                          val userAccomplishmentRows = usernames.toSet[String].map(UserAccomplishmentRow(_, id))
                          val maybeInserted = db.run(UserAccomplishment ++= userAccomplishmentRows)
                          onSuccess(maybeInserted) {
                            case Some(_) => complete(StatusCodes.OK)
                            case None => complete(StatusCodes.BadRequest)
                          }
                        case false => complete(StatusCodes.Unauthorized)
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
                val accomplishments = clean(Accomplishment.filter(_.id >= lower).filter(_.id <= upper))
                complete(accomplishments)
              } else if (range.contains(",")) {
                val ids = range.split(",").map(_.toInt)
                val query = for {
                  m <- Accomplishment if m.id inSetBind ids
                } yield m
                val accomplishments = clean(query)
                complete(accomplishments)
              } else {
                complete(StatusCodes.BadRequest)
              }
            }
          }
      }
    }
}
