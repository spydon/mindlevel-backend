package net.mindlevel.routes

import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

import akka.http.scaladsl.server.Route
import com.github.t3hnar.bcrypt._
import net.mindlevel.models.Tables._
import slick.jdbc.MySQLProfile.api._
import spray.json.DefaultJsonProtocol._
import spray.json.{DeserializationException, JsNumber, JsValue, JsonFormat}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

private object DBSingleton {
  val db = Database.forConfig("db")
}

trait AbstractRoute {
  def route: Route
  protected val db = DBSingleton.db
  protected val accomplishmentPageSize = 20
  protected val challengePageSize = 20

  protected def now(): Long = Instant.now.getEpochSecond

  protected implicit object TimestampFormat extends JsonFormat[Timestamp] {
    def write(obj: Timestamp): JsNumber = JsNumber(obj.getTime)

    def read(json: JsValue): Timestamp = json match {
      case JsNumber(time) => new Timestamp(time.toLong)
      case _ => throw DeserializationException("Timestamp expected")
    }
  }

  // Formats for unmarshalling and marshalling
  protected case class LoginFormat(
      username: String,
      password: Option[String] = None,
      newPassword: Option[String] = None,
      session: Option[String] = None
  )

  protected case class LikeResponse(
      first: Boolean,
      score: String
  )

  protected case class Contributors(
      contributors: List[String]
  )

  protected case class Count(
      count: Int
  )

  protected implicit val accomplishmentFormat = jsonFormat7(AccomplishmentRow)
  protected implicit val challengeFormat = jsonFormat7(ChallengeRow)
  protected implicit val categoryFormat = jsonFormat3(CategoryRow)
  protected implicit val userFormat = jsonFormat6(UserRow)
  protected implicit val userExtraFormat = jsonFormat3(UserExtraRow)
  protected implicit val userAccomplishmentFormat = jsonFormat2(UserAccomplishmentRow)
  protected implicit val loginFormat = jsonFormat4(LoginFormat) // TODO: Refactor this
  protected implicit val likeResponseFormat = jsonFormat2(LikeResponse)
  protected implicit val contributorsFormat = jsonFormat1(Contributors)
  protected implicit val countFormat = jsonFormat1(Count)

  protected case class AuthException(msg: String) extends IllegalAccessException(msg)

  protected def nameFromSession(session: String): Future[Option[String]] = {
    db.run(Session.filter(_.session === session).map(_.username).result.headOption)
  }

  protected def isAuthorized(username: String, session: String): Future[Boolean] = {
    val maybeUser = db.run {
      Session
        .filter(s => s.username === username && s.session === session)
        .result.headOption
    }
    maybeUser.map {
      case Some(_) => true
      case None => false
    }
  }

  protected def isAuthorizedToAccomplishment(accomplishmentId: Int, session: String): Future[Boolean] = {
    val maybeUsername = nameFromSession(session)
    maybeUsername.flatMap {
      case Some(username) =>
        val isAuthorized =
          db.run(UserAccomplishment
            .filter(_.username === username)
            .filter(_.accomplishmentId === accomplishmentId).result.headOption)
        isAuthorized.map {
          case Some(_) => true
          case None => false
        }

      case None =>
        Future(false)
    }
  }

  protected def isAuthorizedToChallenge(challengeId: Int, session: String): Future[Boolean] = {
    val maybeUser = nameFromSession(session)
    maybeUser.flatMap {
      case Some(username) =>
        val isAuthorized =
          db.run(Challenge
            .filter(_.id === challengeId)
            .filter(_.creator === username).result.headOption)
        isAuthorized.map {
          case Some(_) => true
          case None => false
        }

      case None =>
        Future(false)
    }
  }

  protected def updateLastActive(username: String): Future[Boolean] = {
    // Usually just fire and forget also not secured so only call from a secure context
    val now = Some(Instant.now.getEpochSecond)
    val q = for (u <- User if u.username === username) yield u.lastActive
    val maybeUpdated = db.run(q.update(now))
    maybeUpdated map {
      case 1 => true
      case _ => false
    }
  }

  protected def updatePassword(user: LoginFormat): Future[String] = {
    val q = for {
      u <- UserExtra if u.username === user.username
      s <- Session if u.username === s.username
    } yield (u.username, u.password, s.session)

    val maybeAuth = db.run(q.result.headOption)

    maybeAuth flatMap {
      case Some(auth) =>
        auth match {
          case (username, password, session) =>
            if (user.session == session && user.password.getOrElse("").isBcrypted(password)) {
              val q = for (u <- UserExtra if u.username === username) yield u.password
              updateSession(user) flatMap {
                case Some(newSession) =>
                  user.newPassword match {
                    case Some(newPassword) =>
                      val hashedPassword = newPassword.bcrypt
                      val maybeUpdated = db.run(q.update(hashedPassword))
                      maybeUpdated map {
                        case 1 => newSession
                        case _ => throw AuthException(s"Could not update the password")
                      }
                    case None =>
                      throw AuthException(s"No new password sent")
                  }
                case None =>
                  throw AuthException(s"Could not update the session")
              }

            } else {
              // Not authorized
              throw AuthException("Not authorized")
            }
        }
      case None =>
        throw AuthException("Not authorized")
    }
  }

  protected def updateSession(user: LoginFormat, logout: Boolean = false): Future[Option[String]] = {
    val q = for {
      u <- UserExtra if u.username === user.username
      s <- Session if u.username === s.username
    } yield (u.username, u.password, s.session)

    val maybeAuth = db.run(q.result.headOption)

    maybeAuth flatMap {
      case Some(auth) =>
        auth match {
          case (username, password, session) =>
            if ((user.session == session && session.nonEmpty) || user.password.getOrElse("").isBcrypted(password)) {
              val currentSession = for (s <- Session if s.username === username) yield s.session
              val newSession = if (logout) None else Some(UUID.randomUUID().toString)
              val maybeUpdated = db.run(currentSession.update(newSession))
              updateLastActive(username)
              maybeUpdated.map {
                case 1 => newSession
                case _ => throw AuthException(s"Could not update the session")
              }
            } else {
              // Not authorized
              throw AuthException("Invalid password or session")
            }
        }
      case None =>
        throw AuthException("Could not update the session")
    }
  }
}
