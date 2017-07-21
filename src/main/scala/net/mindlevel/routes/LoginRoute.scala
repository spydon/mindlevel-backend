package net.mindlevel.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object LoginRoute extends AbstractRoute {
  def route: Route =
    path("login") {
      post {
        entity(as[LoginFormat]) { login =>
          onSuccess(updateSession(login)) {
            case Some(session) => complete(session)
            case None => complete(StatusCodes.Unauthorized)
          }
        }
      }
    } ~
      path("logout") {
        post {
          entity(as[LoginFormat]) { login =>
            onSuccess(updateSession(login, true)) {
              case None => complete(StatusCodes.OK)
              case _ => complete(StatusCodes.InternalServerError)
            }
          }
        }
      }
}
