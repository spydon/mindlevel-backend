import java.sql.Timestamp

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import slick.jdbc.MySQLProfile.api._
import com.typesafe.config._
import net.mindlevel.models.Tables.{AccomplishmentRow, MissionRow, UserAccomplishmentRow, UserRow}
import net.mindlevel.models.Tables.{Accomplishment, Mission, User, UserAccomplishment}
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
  // Never used for explicit queries?
  //private implicit val userAccomplishmentFormat = jsonFormat2(UserAccomplishmentRow)


  private val accomplishmentRoute =
    get {
      pathPrefix("accomplishment" / IntNumber) { id =>
        // there might be no item for a given id
        val maybeAccomplishment = db.run(Accomplishment.filter(_.id === id).result.headOption)

        onSuccess(maybeAccomplishment) {
          case Some(accomplishment) => complete(accomplishment)
          case None => complete(StatusCodes.NotFound)
        }
      }
    }// ~
  // post {
  //   path("create-order") {
  //     entity(as[Order]) { order =>
  //       //val saved: Future[Done] = saveOrder(order)
  //       val saved: Future[Done] = Future(Done)
  //       onComplete(saved) { done =>
  //         complete("order created")
  //       }
  //     }
  //   }
  // }

  private val missionRoute =
    get {
      pathPrefix("mission" / IntNumber) { id =>
        // there might be no item for a given id
        val maybeMission = db.run(Mission.filter(_.id === id).result.headOption)

        onSuccess(maybeMission) {
          case Some(mission) => complete(mission)
          case None => complete(StatusCodes.NotFound)
        }
      }
    }

  private val userRoute =
    get {
      pathPrefix("user" / Segment) { username =>
        // there might be no item for a given id
        val maybeUser = db.run(User.filter(_.username === username).result.headOption)

        onSuccess(maybeUser) {
          case Some(user) => complete(user)
          case None => complete(StatusCodes.NotFound)
        }
      }
    }

  val all: Route = accomplishmentRoute ~ missionRoute ~ userRoute

}
