import java.sql.Timestamp

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import spray.json.{DeserializationException, JsNumber, JsValue, JsonFormat}
import slick.jdbc.MySQLProfile.api._
import net.mindlevel.models.Tables._
import com.github.t3hnar.bcrypt._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

object Routes {
  private val db = Database.forConfig("db")
  private val accomplishmentPageSize = 20

  private implicit object TimestampFormat extends JsonFormat[Timestamp] {
    def write(obj: Timestamp): JsNumber = JsNumber(obj.getTime)

    def read(json: JsValue): Timestamp = json match {
      case JsNumber(time) => new Timestamp(time.toLong)
      case _ => throw DeserializationException("Timestamp expected")
    }
  }

  // Formats for unmarshalling and marshalling
  private case class LoginFormat(
     username: String,
     password: Option[String] = None,
     newPassword: Option[String] = None,
     session: Option[String] = None
  )
  private case class ContributorRequest(
     usernames: Seq[String],
     session: String
  )
  private implicit val accomplishmentFormat = jsonFormat7(AccomplishmentRow)
  private implicit val missionFormat = jsonFormat7(MissionRow)
  private implicit val userFormat = jsonFormat7(UserRow)
  private implicit val userAccomplishmentFormat = jsonFormat2(UserAccomplishmentRow)
  private implicit val loginFormat = jsonFormat4(LoginFormat)
  private implicit val contributorRequestFormat = jsonFormat2(ContributorRequest)

  private case class SessionUpdateException(msg: String, cause: Throwable = null) extends RuntimeException(msg, cause)

  private val accomplishmentRoute =
    pathPrefix("accomplishment") {
      pathEndOrSingleSlash {
        get {
          complete(db.run(Accomplishment.result))
        }
        post {
          entity(as[AccomplishmentRow]) { accomplishment =>
            val zeroScoreAccomplishment = accomplishment.copy(score = 0)
            val maybeInserted = db.run(Accomplishment += zeroScoreAccomplishment)
            onSuccess(maybeInserted) {
              case 1 => complete(StatusCodes.OK)
              case _ => complete(StatusCodes.BadRequest)
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
                val drop = between(0).toInt-1
                val upper = between(1).toInt
                val take = upper-drop
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
          path("contributor") {
            get {
              onSuccess(maybeAccomplishment) {
                case Some(accomplishment) =>
                  val contributors =
                    db.run(UserAccomplishment.filter(_.accomplishmentId === accomplishment.id).map(_.username).result)
                  onSuccess(contributors)(complete(_))
              }
            } ~
              post {
                entity(as[ContributorRequest]) { request =>
                  onSuccess(maybeAccomplishment) {
                    case Some(_) =>
                      onSuccess(nameFromSession(request.session)) {
                        case Some(requestor) =>
                          val isAuthorized = db.run(UserAccomplishment
                            .filter(_.username === requestor)
                            .filter(_.accomplishmentId === id).result.headOption)
                          onSuccess(isAuthorized) {
                            case Some(_) =>
                              val userAccomplishmentRows = request.usernames.map(UserAccomplishmentRow(_, id))
                              val maybeInserted = db.run(UserAccomplishment ++= userAccomplishmentRows)
                              onSuccess(maybeInserted) {
                                case Some(_) => complete(StatusCodes.OK)
                                case None => complete(StatusCodes.BadRequest)
                              }
                            case None => complete(StatusCodes.Unauthorized)
                          }
                        case None => complete(StatusCodes.BadRequest)
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

  private val missionRoute =
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
          get {
            val maybeMission = db.run(Mission.filter(_.id === id).result.headOption)

            onSuccess(maybeMission) {
              case Some(mission) => complete(mission)
              case None => complete(StatusCodes.NotFound)
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

  private val userRoute =
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
                case Some(user) => complete(user)
                case None => complete(StatusCodes.NotFound)
              }
            } ~
              put {
                entity(as[UserRow]) { user =>
                  onSuccess(isAuthorized(user.username, user.session.getOrElse(""))) {
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
                } ~
                entity(as[LoginFormat]) { user =>
                  onSuccess(updatePassword(user)) { session =>
                    complete(session)
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

  private val loginRoute =
    path("login") {
      post {
        entity(as[LoginFormat]) { login =>
          onSuccess(updateSession(login)) {
            case Some(session) => complete(session)
            case None => complete(StatusCodes.Unauthorized)
          }
        }
      }
    } ~
      path("logout") {
        post {
          entity(as[LoginFormat]) { login =>
            onSuccess(updateSession(login, true)) {
              case None => complete(StatusCodes.OK)
              case _ => complete(StatusCodes.InternalServerError)
            }
          }
        }
      }

  private def nameFromSession(session: String) = {
    db.run(User.filter(_.session === session).map(_.username).result.headOption)
  }

  private def isAuthorized(username: String, session: String): Future[Boolean] = {
    val maybeUser = db.run {
      User
        .filter(user => user.username === username && user.session === session)
        .result.headOption
    }
    maybeUser.map {
      case Some(_) => true
      case None => false
    }
  }

  private def updatePassword(user: LoginFormat): Future[String] = {
     // Rewrite as transaction, could potentially cause race conditions or timing attacks
    val maybeUser = db.run {
      User
        .filter(_.username === user.username)
        .result.headOption
    }

    maybeUser flatMap {
      case Some(foundUser) =>
        if (user.session == foundUser.session && user.password.getOrElse("").isBcrypted(foundUser.password)) {
          val q = for (u <- User if u.username === foundUser.username) yield (u.password, u.session)
          val newSession = Some(Random.alphanumeric.take(64).mkString)
          user.newPassword match {
            case Some(password) =>
              val hashedPassword = password.bcrypt
              val maybeUpdated = db.run(q.update(hashedPassword, newSession))
              maybeUpdated.map {
                case 1 => newSession.get
                case _ => throw SessionUpdateException(s"Could not update the session")
              }
            case None =>
              throw SessionUpdateException(s"Could not update the session")
          }

        } else {
          // Not authorized
          throw SessionUpdateException("Could not update the session")
        }
      case None =>
        throw SessionUpdateException("Could not update the session")
    }
  }

  private def updateSession(user: LoginFormat, logout: Boolean = false): Future[Option[String]] = {
    // Rewrite as transaction, could potentially cause race conditions or timing attacks
    val maybeUser = db.run {
      User
        .filter(_.username === user.username)
        .result.headOption
    }

    maybeUser flatMap {
      case Some(foundUser) =>
        if (user.session == foundUser.session || user.password.getOrElse("").isBcrypted(foundUser.password)) {
          val currentSession = for (u <- User if u.username === foundUser.username) yield u.session
          val newSession = if (logout) None else Some(Random.alphanumeric.take(64).mkString)
          val maybeUpdated = db.run(currentSession.update(newSession))
          maybeUpdated.map {
            case 1 => newSession
            case _ => throw SessionUpdateException(s"Could not update the session")
          }
        } else {
          // Not authorized
          throw SessionUpdateException("Could not update the session")
        }
      case None =>
        throw SessionUpdateException("Could not update the session")
    }
  }

  def all: Route = accomplishmentRoute ~ missionRoute ~ userRoute ~ loginRoute

}
