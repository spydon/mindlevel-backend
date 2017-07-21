package net.mindlevel.routes

import java.nio.file.Paths

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.FileIO
import net.mindlevel.Implicits
import net.mindlevel.models.Tables._
import slick.jdbc.MySQLProfile.api._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object AccomplishmentRoute extends AbstractRoute with Implicits {

  def route: Route =
    pathPrefix("accomplishment") {
      pathEndOrSingleSlash {
        get {
          complete(db.run(Accomplishment.result))
        }
        post {
          entity(as[AccomplishmentRow]) { accomplishment =>
            val zeroScoreAccomplishment = accomplishment.copy(score = 0)
            val maybeInserted = db.run(Accomplishment += zeroScoreAccomplishment)
            onSuccess(maybeInserted) {
              case 1 => complete(StatusCodes.OK)
              case _ => complete(StatusCodes.BadRequest)
            }
          }
        }
      } ~
        pathPrefix("latest") {
          pathEndOrSingleSlash {
            get {
              val accomplishments = db.run(Accomplishment.sortBy(_.created.desc).take(accomplishmentPageSize).result)
              complete(accomplishments)
            }
          } ~
            path(IntNumber) { pageSize =>
              get {
                val accomplishments = db.run(Accomplishment.sortBy(_.created.desc).take(pageSize).result)
                complete(accomplishments)
              }
            } ~
            path(Segment) { range =>
              get {
                if (range.contains("-")) {
                  val between = range.split("-")
                  val drop = between(0).toInt - 1
                  val upper = between(1).toInt
                  val take = upper - drop
                  if (drop < upper) {
                    val accomplishments = db.run(Accomplishment.sortBy(_.created.desc).drop(drop).take(take).result)
                    complete(accomplishments)
                  } else {
                    complete(StatusCodes.BadRequest)
                  }
                } else {
                  complete(StatusCodes.BadRequest)
                }
              }
            }
        } ~
        pathPrefix(IntNumber) { id =>
          val maybeAccomplishment = db.run(Accomplishment.filter(_.id === id).result.headOption)
          pathEndOrSingleSlash {
            get {
              onSuccess(maybeAccomplishment) {
                case Some(accomplishment) => complete(accomplishment)
                case None => complete(StatusCodes.NotFound)
              }
            } ~
              path("image") {
                post {
                  fileUpload("image") {
                    case (fileInfo, fileStream) =>
                      val sink = FileIO.toPath(Paths.get("/tmp") resolve fileInfo.fileName)
                      val writeResult = fileStream.runWith(sink)
                      onSuccess(writeResult) { result =>
                        result.status match {
                          case Success(_) => complete(s"Successfully written ${result.count} bytes")
                          case Failure(e) => throw e
                        }
                      }
                  }
                }
              } ~
              path("contributor") {
                get {
                  onSuccess(maybeAccomplishment) {
                    case Some(accomplishment) =>
                      val contributors =
                        db.run(UserAccomplishment.filter(_.accomplishmentId === accomplishment.id).map(_.username).result)
                      onSuccess(contributors)(complete(_))
                  }
                } ~
                  post {
                    entity(as[ContributorRequest]) { request =>
                      onSuccess(maybeAccomplishment) {
                        case Some(_) =>
                          onSuccess(nameFromSession(request.session)) {
                            case Some(requestor) =>
                              val isAuthorized = db.run(UserAccomplishment
                                .filter(_.username === requestor)
                                .filter(_.accomplishmentId === id).result.headOption)
                              onSuccess(isAuthorized) {
                                case Some(_) =>
                                  val userAccomplishmentRows = request.usernames.map(UserAccomplishmentRow(_, id))
                                  val maybeInserted = db.run(UserAccomplishment ++= userAccomplishmentRows)
                                  onSuccess(maybeInserted) {
                                    case Some(_) => complete(StatusCodes.OK)
                                    case None => complete(StatusCodes.BadRequest)
                                  }
                                case None => complete(StatusCodes.Unauthorized)
                              }
                            case None => complete(StatusCodes.BadRequest)
                          }
                        case None => complete(StatusCodes.NotFound)
                      }
                    }
                  }
              }
          } ~
            path(Segment) { range =>
              get {
                if (range.contains("-")) {
                  val between = range.split("-")
                  val lower = between(0).toInt
                  val upper = between(1).toInt
                  val accomplishments = db.run(Accomplishment.filter(_.id >= lower).filter(_.id <= upper).result)
                  complete(accomplishments)
                } else if (range.contains(",")) {
                  val ids = range.split(",").map(_.toInt)
                  val query = for {
                    m <- Accomplishment if m.id inSetBind ids
                  } yield m
                  val accomplishments = db.run(query.result)
                  complete(accomplishments)
                } else {
                  complete(StatusCodes.BadRequest)
                }
              }
            }
        }
    }
}
