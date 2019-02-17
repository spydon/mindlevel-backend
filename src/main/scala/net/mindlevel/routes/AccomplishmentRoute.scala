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

import net.mindlevel.TimeUtil._

object AccomplishmentRoute extends AbstractRoute {

  private type AccomplishmentQuery = Query[Accomplishment, Accomplishment#TableElementType, Seq]

  def removeRestricted(db: Database, session: String)(accomplishments: AccomplishmentQuery):
  Future[Seq[Accomplishment#TableElementType]] = {
    userFromSession(db, session).map { user =>
      accomplishments.filter(_.scoreRestriction <= user.score).filter(_.levelRestriction <= user.level)
    }.flatMap(q => db.run(q.result))
  }

  def route: Route =
    pathPrefix("accomplishment") {
      database { db =>
        sessionId() { session =>
          def clean = removeRestricted(db, session)(_)

          def cleanHead(query: AccomplishmentQuery) =
            removeRestricted(db, session)(query).map(_.headOption)

          pathEndOrSingleSlash {
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
                        Unmarshal(parts("accomplishment")).to[AccomplishmentRow].map { accomplishmentRow =>
                          val level = accomplishmentRow.levelRestriction
                          accomplishmentRow.copy(
                            image = parts("image"),
                            score = 0,
                            created = Some(now()),
                            levelRestriction = 0, // Show all accomplishments for now, otherwise change to level
                            scoreRestriction = 0
                          )
                        }
                      }

                      val contributorsF = allParts.flatMap { parts =>
                        Unmarshal(parts("contributors")).to[Contributors].map(_.contributors)
                      } map { usernames => (username :: usernames).toSet }

                      def updateLevelCount(username: String): Future[Int] = {
                        val user = for {u <- User if u.username === username} yield u.level
                        // Take out all accomplishments for a user and count the unique ones
                        val levelQuery = (for {
                          (a, ua) <- Accomplishment join UserAccomplishment on (_.id === _.accomplishmentId)
                          if ua.username === username
                        } yield a.challengeId).distinct.size
                        db.run(levelQuery.result).flatMap[Int] { level =>
                          db.run(user.update(Some(level)))
                        }
                      }

                      onSuccess(row) { accomplishment =>
                        val accomplishmentWithIdQuery =
                          Accomplishment returning Accomplishment.map(_.id) into ((accomplishment: AccomplishmentRow, id) =>
                            accomplishment.copy(id = id)) += accomplishment
                        onSuccess(db.run(accomplishmentWithIdQuery)) { accomplishment =>
                          if (accomplishment.challengeId == 1) {
                            // Update users profile pic if this was accomplishment 1
                            val user = for {u <- User if u.username === username} yield u.image
                            db.run(user.update(Some(accomplishment.image)))
                          }

                          onSuccess(contributorsF) { contributors =>
                            val contributorRows =
                              contributors.map { contributor =>
                                UserAccomplishmentRow(username = contributor, accomplishmentId = accomplishment.id)
                              }
                            // TODO: Contributors need to be checked whether or not they are allowed to finish the challenge
                            onSuccess(db.run(UserAccomplishment ++= contributorRows)) { _ =>
                              contributors.foreach(u => updateLevelCount(u))
                              complete(accomplishment)
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
                  get { // TODO: Should be POST or PUT
                    onSuccess(nameFromSession(db, session)) {
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

                              onSuccess(db.run(updateScore)) {
                                case 0 => scoreResponse(false)
                                case _ => scoreResponse(true)
                              }
                            case _ =>
                              // Already liked or user does not have 1 accomplishment yet
                              scoreResponse(false)
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
                        onSuccess(isAuthorizedToAccomplishment(db, id, session)) {
                          case true =>
                            val userAccomplishmentRows = usernames.toSet[String].map(UserAccomplishmentRow(_, id))
                            // TODO: Ignore duplicate entry exceptions
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
                  val accomplishments =
                    clean(Accomplishment.filter(_.id >= lower).filter(_.id <= upper).sortBy(_.created.desc))
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
}
