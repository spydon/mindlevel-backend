package net.mindlevel.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

object Routes {
  private val routes = Seq(AccomplishmentRoute, LoginRoute, ChallengeRoute, PingRoute, UserRoute, StatsRoute,
                           CustomRoute, NotificationRoute, CommentRoute)

  def all: Route = cors() { routes.map(_.route).reduceLeft(_ ~ _) }
}
