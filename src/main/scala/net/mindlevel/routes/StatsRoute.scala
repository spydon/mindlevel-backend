package net.mindlevel.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import net.mindlevel.models.Tables._
import slick.jdbc.MySQLProfile.api._
import slick.lifted.AbstractTable
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object StatsRoute extends AbstractRoute {
  def count[T <: AbstractTable[_]](table: TableQuery[T]): Future[Count] = {
    db.run(table.size.result).map(Count)
  }

  def route: Route =
    pathPrefix("stats") {
      path("usernames") {
        get {
          val usernames = db.run(User.map(_.username).result)
          onSuccess(usernames)(complete(_))
        }
      } ~
      pathPrefix("count") {
        path("user") {
          get {
            onSuccess(count[User](User))(complete(_))
          }
        } ~
        path("accomplishment") {
          get {
            onSuccess(count[Accomplishment](Accomplishment))(complete(_))
          }
        } ~
        path("challenge") {
          get {
            onSuccess(count[Challenge](Challenge))(complete(_))
          }
        }
      } ~
      pathPrefix("latest") {
        path("user") {
          get {
            val user = db.run(User.sortBy(_.created.desc).take(1).result)
            onSuccess(user)(complete(_))
          }
        } ~
        path("accomplishment") {
          get {
            val accomplishment = db.run(Accomplishment.sortBy(_.created.desc).take(1).result)
            onSuccess(accomplishment)(complete(_))
          }
        } ~
        path("challenge") {
          get {
            val challenge = db.run(Challenge.sortBy(_.created.desc).take(1).result)
            onSuccess(challenge)(complete(_))
          }
        }
      } ~
      pathPrefix("highscore") {
        path(IntNumber) { amount =>
          get {
            val users = db.run(User.sortBy(_.score.desc).take(amount).result)
            onSuccess(users)(complete(_))
          }
        }
      }
    }
}
