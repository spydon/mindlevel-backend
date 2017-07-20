package routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.t3hnar.bcrypt._
import net.mindlevel.models.Tables._
import slick.jdbc.MySQLProfile.api._
import spray.json.DefaultJsonProtocol._

object MissionRoute extends AbstractRoute {
  def route: Route =
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
}
