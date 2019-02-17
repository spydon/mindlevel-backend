package net.mindlevel

import akka.actor.Actor
import net.mindlevel.models.Tables.{AccomplishmentRow, ChallengeRow, CommentRow, NotificationRow, NotificationUserRow}
import net.mindlevel.models.Tables.{Accomplishment, Comment, Notification, NotificationUser, UserAccomplishment}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

case class CommentUpdate(comment: CommentRow, db: Database)
case class AccomplishmentUpdate(accomplishment: AccomplishmentRow, db: Database)
case class ChallengeUpdate(challenge: ChallengeRow, db: Database)

class NotificationService extends Actor {
  private def generateFromComment(comment: CommentRow, db: Database): Unit = {
    if (comment.threadId == 0) { // ignore comments from the chat for now
      return
    }

    val id = comment.id
    val accomplishmentId = comment.threadId
    val accomplishmentF = db.run(Accomplishment.filter(_.id === accomplishmentId).result)
    val creatorsF = db.run(UserAccomplishment.filter(_.accomplishmentId === accomplishmentId).map(_.username).result)
    val repliersF = db.run(Comment.filter(_.threadId === accomplishmentId).map(_.username).result)

    for {
      maybeAccomplishment <- accomplishmentF
      creators <- creatorsF
      repliers <- repliersF
    } yield {
      val accomplishment = maybeAccomplishment.head // If it doesn't exist something is majorly wrong
      val users = (creators ++ repliers).toSet - comment.username
      val title = s"New comment!"
      val description = s"""${comment.username} replied on "${accomplishment.title}""""
      val timestamp = TimeUtil.timestamp()
      val notificationQuery =
        db.run(Notification returning Notification.map(_.id) +=
          NotificationRow(
            id = 0, title = title, description = description, created = timestamp, `type` = Some("comment")
          )
        )
      notificationQuery map { notificationId =>
        val userNotifications = users map { username =>
          NotificationUserRow(notificationId, username, false)
        }

        Await.result(db.run(NotificationUser ++= userNotifications), 5.seconds)
      }
    }
  }

  private def generateFromAccomplishment(accomplishment: AccomplishmentRow, db: Database): Unit = {
    // Do nothing, yet
  }

  private def generateFromChallenge(challenge: ChallengeRow, db: Database): Unit = {
    // Do nothing, yet
  }

  def receive = {
    case CommentUpdate(comment, db) =>
      generateFromComment(comment, db)
    case AccomplishmentUpdate(accomplishment, db) =>
      generateFromAccomplishment(accomplishment, db)
    case ChallengeUpdate(challenge, db) =>
      generateFromChallenge(challenge, db)
    case _ =>
      println("NotificationService got something unexpected.")
  }
}
