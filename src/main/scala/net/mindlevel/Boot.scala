package net.mindlevel

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import net.mindlevel.routes.Routes

import scala.concurrent.ExecutionContextExecutor

object Boot extends App {
  // needed to run the route
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val conf = ConfigFactory.load()
  val hostname = conf.getString("mindlevel.hostname")
  val port = conf.getInt("mindlevel.port")
  val jdbc = conf.getString("db.default.url")
  // needed for the future map/flatmap in the end
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val bindingFuture = Http().bindAndHandle(Routes.all, hostname, port)
  val notificationService = system.actorOf(Props[NotificationService], name = "notifications")
  println(s"Server online at http://$hostname:$port/\nJDBC Host: $jdbc\nSend SIGTERM to stop...")
  // TODO: Handle ctrl+c gracefully
}