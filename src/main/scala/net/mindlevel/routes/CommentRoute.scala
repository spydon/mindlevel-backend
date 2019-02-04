package net.mindlevel.routes


import java.sql.Timestamp

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import net.mindlevel.models.Tables._
import slick.jdbc.MySQLProfile.api._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext.Implicits.global

object CommentRoute extends AbstractRoute {

  private type CommentQuery = Query[Comment, Comment#TableElementType, Seq]

  def route: Route =
    pathPrefix("comment") {
      database { db =>
        sessionId { session =>
          pathEndOrSingleSlash {
            post {
              entity(as[CommentRow]) { comment =>
                onSuccess(nameFromSession(db, session)) {
                  case Some(username) =>
                    val userComment = comment.copy(username = username, created = new Timestamp(now()))
                    val maybeUpdated = db.run(Comment += userComment)
                    onSuccess(maybeUpdated) {
                      case 1 => complete(StatusCodes.OK)
                      case _ => complete(StatusCodes.BadRequest)
                    }
                  case None =>
                    complete(StatusCodes.Unauthorized)
                }
              }
            }
          } ~
            pathPrefix("thread") {
              pathPrefix(IntNumber) { threadId =>
                pathEndOrSingleSlash {
                  get {
                    // Thread Id is accomplishment id for the time being, expand when needed
                    val comments = db.run(Comment.filter(_.threadId === threadId).sortBy(_.created.asc).result)
                    complete(comments)
                  }
                } ~
                path("count") {
                  complete(db.run(Comment.filter(_.threadId === threadId).size.result.map(Count)))
                }
              }
            } ~
            pathPrefix(IntNumber) { id =>
              pathEndOrSingleSlash {
                get {
                  complete(db.run(Comment.filter(_.id === id).result.headOption))
                }
              }
            }
        }
      }
    }
}
