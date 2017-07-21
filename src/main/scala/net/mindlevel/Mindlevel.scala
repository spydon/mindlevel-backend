package net.mindlevel

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import net.mindlevel.routes.Routes

import scala.io.StdIn

trait Implicits {
  // needed to run the route
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  // needed for the future map/flatmap in the end
  implicit val executionContext = system.dispatcher
}

object Mindlevel extends App with Implicits {
  val bindingFuture = Http().bindAndHandle(Routes.all, "localhost", 8080)
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ ⇒ system.terminate()) // and shutdown when done
}