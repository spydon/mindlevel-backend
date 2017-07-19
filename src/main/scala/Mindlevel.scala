import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.Done
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import slick.jdbc.MySQLProfile.api._
import com.typesafe.config._
import net.mindlevel.models.Tables.{AccomplishmentRow, MissionRow, UserAccomplishmentRow, UserRow}

import scala.io.StdIn
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Mindlevel {
  val conf = ConfigFactory.load()
  val db = Database.forConfig("db")

  // formats for unmarshalling and marshalling
  implicit val itemFormat = jsonFormat7(AccomplishmentRow)
  implicit val missionFormat = jsonFormat7(MissionRow)
  implicit val userFormat = jsonFormat7(UserRow)
  implicit val userAccomplishmentFormat = jsonFormat2(UserAccomplishmentRow)

  // (fake) async database query api
  //def fetchItem(itemId: Long): Future[Option[Item]] = Future(Some(Item("name", 123)))
  //def saveOrder(order: Order): Future[Done] = Future(Done)

  def main(args: Array[String]) {

    // needed to run the route
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    // needed for the future map/flatmap in the end
    implicit val executionContext = system.dispatcher

    val route: Route =
      get {
        pathPrefix("item" / LongNumber) { id =>
          // there might be no item for a given id
          //val maybeItem: Future[Option[Item]] = fetchItem(id)
          val maybeItem: Future[Option[Item]] = Future(None)

          onSuccess(maybeItem) {
            case Some(item) => complete(item)
            case None       => complete(StatusCodes.NotFound)
          }
        }
      } ~
        post {
          path("create-order") {
            entity(as[Order]) { order =>
              //val saved: Future[Done] = saveOrder(order)
              val saved: Future[Done] = Future(Done)
              onComplete(saved) { done =>
                complete("order created")
              }
            }
          }
        }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ ⇒ system.terminate()) // and shutdown when done

  }
}