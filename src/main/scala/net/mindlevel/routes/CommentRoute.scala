package net.mindlevel.routes


import java.sql.Timestamp

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import net.mindlevel.Boot.notificationService
import net.mindlevel.CommentUpdate
import net.mindlevel.models.Tables._
import slick.jdbc.MySQLProfile.api._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext.Implicits.global
import net.mindlevel.TimeUtil._

object CommentRoute extends AbstractRoute {

  private type CommentQuery = Query[Comment, Comment#TableElementType, Seq]

  def route: Route =
    pathPrefix("comment") {
      database { db =>
        sessionId() { session =>
          pathEndOrSingleSlash {
            post {
              entity(as[CommentRow]) { comment =>
                onSuccess(nameFromSession(db, session)) {
                  case Some(username) =>
                    val userComment = comment.copy(username = username, created = timestamp())
                    val maybeUpdated = db.run((Comment returning Comment.map(_.id)) += userComment)

                    onSuccess(maybeUpdated) { id =>
                      notificationService ! CommentUpdate(userComment.copy(id = id), db)
                      complete(StatusCodes.OK)
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
                pathPrefix("since") {
                  path(LongNumber) { timestamp => // Unix time
                    // Thread Id is accomplishment id for the time being, expand when needed
                    val comments =
                      db.run(Comment.filter(_.threadId === threadId)
                                    .filter(_.created > new Timestamp(timestamp))
                                    .sortBy(_.created.asc).result)
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
