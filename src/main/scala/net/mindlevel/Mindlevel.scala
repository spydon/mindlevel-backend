package net.mindlevel

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}
import net.mindlevel.routes.Routes

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

trait Implicits {
  private val configs = ConfigFactory.load()
  private val actorSystemName = configs.getString("mindlevel.actorsystem")
  // needed to run the route
  implicit val system = ActorSystem(actorSystemName)
  implicit val materializer = ActorMaterializer()

  // needed for the future map/flatmap in the end
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
}

object Mindlevel extends App with Implicits {
  val bindingFuture = Http().bindAndHandle(Routes.all, "localhost", 8080)
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ ⇒ system.terminate()) // and shutdown when done
}