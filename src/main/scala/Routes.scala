import java.sql.Timestamp

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import slick.jdbc.MySQLProfile.api._
import com.typesafe.config._
import net.mindlevel.models.Tables.{AccomplishmentRow, MissionRow, UserRow}
import net.mindlevel.models.Tables.{Accomplishment, Mission, User}
import spray.json.{DeserializationException, JsNumber, JsValue, JsonFormat}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Routes {
  private val conf = ConfigFactory.load()
  private val db = Database.forConfig("db")

  implicit object TimestampFormat extends JsonFormat[Timestamp] {
    def write(obj: Timestamp) = JsNumber(obj.getTime)

    def read(json: JsValue) = json match {
      case JsNumber(time) => new Timestamp(time.toLong)
      case _ => throw DeserializationException("Timestamp expected")
    }
  }

  // Formats for unmarshalling and marshalling
  private implicit val accomplishmentFormat = jsonFormat7(AccomplishmentRow)
  private implicit val missionFormat = jsonFormat7(MissionRow)
  private implicit val userFormat = jsonFormat7(UserRow)

  private val accomplishmentRoute =
    pathPrefix("accomplishment") {
      pathEndOrSingleSlash {
        get {
          complete(db.run(Accomplishment.result))
        }
      } ~
        path(IntNumber) { id =>
          get {
            val maybeAccomplishment = db.run(Accomplishment.filter(_.id === id).result.headOption)

            onSuccess(maybeAccomplishment) {
              case Some(accomplishment) => complete(accomplishment)
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
    get {
      pathPrefix("user" / Segment) { username =>
        val maybeUser = db.run(User.filter(_.username === username).result.headOption)

        onSuccess(maybeUser) {
          case Some(user) => complete(user)
          case None => complete(StatusCodes.NotFound)
        }
      }
    }

  def all: Route = accomplishmentRoute ~ missionRoute ~ userRoute

}
