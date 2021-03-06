package net.mindlevel.routes

import java.time.{LocalDateTime, ZoneOffset}

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

  def countDb[T <: AbstractTable[_]](db: Database)(table: TableQuery[T]): Future[Count] = {
    db.run(table.size.result).map(Count)
  }

  def route: Route =
    pathPrefix("stats") {
      database { db =>
        def count[T <: AbstractTable[_]] = countDb[T](db)(_)
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
              path("comment") {
                get {
                  onSuccess(count[Comment](Comment))(complete(_))
                }
              } ~
              path("challenge") {
                get {
                  onSuccess(count[Challenge](Challenge))(complete(_))
                }
              } ~
              path("active") {
                get {
                  val count = db.run(
                    User.filter(_.lastActive > LocalDateTime.now().minusWeeks(1).toEpochSecond(ZoneOffset.UTC)).size.result
                  ).map(Count)
                  onSuccess(count)(complete(_))
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
              path("username") {
                get {
                  val username = db.run(User.sortBy(_.created.desc).take(1).map(_.username).result)
                  onSuccess(username)(complete(_))
                }
              } ~
              path("comment") {
                get {
                  val comment = db.run(Comment.sortBy(_.created.desc).take(1).result)
                  onSuccess(comment)(complete(_))
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
              } ~
              path("active") {
                get {
                  val user = db.run(User.sortBy(_.lastActive.desc).take(1).result)
                  onSuccess(user)(complete(_))
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
}
