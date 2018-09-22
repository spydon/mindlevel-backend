package net.mindlevel.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

object Routes {
  private val routes = Seq(AccomplishmentRoute, LoginRoute, ChallengeRoute,
                           PingRoute, UserRoute, StatsRoute, CustomRoute)

  object AllRoutes extends AbstractRoute {
    val route: Route =
      cors() {
        optionalHeaderValueByName("X-Integration") { integration =>
          routes.map( route => {
            route.setIntegration(integration)
            route.route
          }).reduceLeft(_ ~ _)
        }
      }
  }
}
