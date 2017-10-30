package net.mindlevel.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import net.mindlevel.models.Tables._
import slick.jdbc.MySQLProfile.api._

object PingRoute extends AbstractRoute {
  def route: Route =
    path("ping") {
      get {
        val maybeResult = db.run(Challenge.take(1).result.headOption)

        onSuccess(maybeResult) {
          case Some(_) => complete(StatusCodes.OK)
          case None => complete(StatusCodes.InternalServerError)
        }
      }
    }
}
