package net.mindlevel.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import net.mindlevel.models.Tables.CustomDb
import slick.jdbc.MySQLProfile.api._

object CustomRoute extends AbstractRoute {
  def route: Route =
    pathPrefix("custom") {
      path(Segment) { pass =>
        get {
          val customDb = db.run(CustomDb.filter(_.pass === pass).result.headOption)
          onSuccess(customDb)(complete(_))
        }
      }
    }
}
