package routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

object Routes {
  private val routes = Seq(AccomplishmentRoute, LoginRoute, MissionRoute, UserRoute)
  def all: Route = routes.map(_.route).reduceLeft(_ ~ _)
}
