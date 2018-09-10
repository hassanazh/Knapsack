package com.knapsack

import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.knapsack.model.Knapsack
import com.knapsack.route.KnapsackRoute
import com.knapsack.service.{AuthService, SolutionService, TaskService}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.util.control.NonFatal

object KnapsackApp extends App with LazyLogging {

  private implicit val system: ActorSystem = ActorSystem("knapscak-service")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()
  private implicit val ec: ExecutionContextExecutor = system.dispatcher

  private val DefaultBindTimeout = 59 seconds
  private val DefaultShutdownTimeout = 29 seconds
  import com.typesafe.config.ConfigFactory

  val apiConfig = ConfigFactory.load().getConfig("com.knapsack.api")

  try {
    val taskService: TaskService = new TaskService()
    val knapsack = new Knapsack()
    val solutionService: SolutionService = new SolutionService(taskService, knapsack)
    val authService = new AuthService()
    val knaksackRoute = new KnapsackRoute(system, DefaultShutdownTimeout, taskService, solutionService, authService)

    val bindingFuture = Http().bindAndHandle(knaksackRoute.route, apiConfig.getString("http.host"), apiConfig.getInt("http.port"))
    val serverBinding = Await.result(bindingFuture, DefaultBindTimeout)
    logger.info(s"Http server started to listen to ${serverBinding.localAddress}")
    sys.addShutdownHook {
      Await.ready(bindingFuture.flatMap(_.unbind()).flatMap(_ => system.terminate()), DefaultShutdownTimeout)
      logger.info("Http server stopped")
      logger.info("Knapsack service stopped")
    }
  } catch {
    case NonFatal(e) =>
      logger.error(s"Knapsack service crashed", e)
      Await.ready(system.terminate(), DefaultShutdownTimeout)
      System.exit(1)
  }
}
