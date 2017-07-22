package net.mindlevel.routes

import java.sql.Timestamp

import akka.http.scaladsl.server.Route
import com.github.t3hnar.bcrypt._
import net.mindlevel.models.Tables._
import slick.jdbc.MySQLProfile.api._
import spray.json.DefaultJsonProtocol._
import spray.json.{DeserializationException, JsNumber, JsValue, JsonFormat}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random


trait AbstractRoute {
  def route: Route
  protected val db = Database.forConfig("db")
  protected val accomplishmentPageSize = 20

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

  protected implicit val accomplishmentFormat = jsonFormat7(AccomplishmentRow)
  protected implicit val missionFormat = jsonFormat7(MissionRow)
  protected implicit val userFormat = jsonFormat7(UserRow)
  protected implicit val userAccomplishmentFormat = jsonFormat2(UserAccomplishmentRow)
  protected implicit val loginFormat = jsonFormat4(LoginFormat)

  protected case class SessionUpdateException(msg: String, cause: Throwable = null) extends RuntimeException(msg, cause)

  protected def nameFromSession(session: String) = {
    db.run(User.filter(_.session === session).map(_.username).result.headOption)
  }

  protected def isAuthorized(username: String, session: String): Future[Boolean] = {
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

  protected def isAuthorizedToAccomplishment(accomplidhmentId: Int, session: String): Future[Boolean] = {
    val maybeUser = nameFromSession(session)
    maybeUser.flatMap {
      case Some(username) =>
        val isAuthorized =
          db.run(UserAccomplishment
            .filter(_.username === username)
            .filter(_.accomplishmentId === accomplidhmentId).result.headOption)
        isAuthorized.map {
          case Some(_) => true
          case None => false
        }

      case None =>
        Future(false)
    }
  }

  protected def updatePassword(user: LoginFormat): Future[String] = {
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

  protected def updateSession(user: LoginFormat, logout: Boolean = false): Future[Option[String]] = {
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

}
