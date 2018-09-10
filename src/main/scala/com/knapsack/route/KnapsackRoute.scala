package com.knapsack.route

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.knapsack.http.HttpErrorHandling
import com.knapsack.model.CirceFormatters._
import com.knapsack.model.{Problems, Status, SystemStatus}
import com.knapsack.service.{AuthService, SolutionService, TaskService}
import com.typesafe.scalalogging.LazyLogging
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.Printer

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration

class KnapsackRoute(system: ActorSystem, DefaultShutdownTimeout: FiniteDuration, taskService: TaskService, solutionService: SolutionService, authService: AuthService)
                   (implicit val ec: ExecutionContext) extends HttpErrorHandling
  with LazyLogging {
  implicit val printer: Printer = Printer.noSpaces.copy(dropNullValues = true, preserveOrder = true)

  val route: Route = rejectEmptyResponse {
    authenticateBasic(realm = "secure site", authService.myUserPassAuthenticator) { user =>
      handleExceptions(exceptionHandler) {
        pathPrefix("knapsack") {
          pathPrefix("tasks" / Segment) { taskId =>
            pathEndOrSingleSlash {
              get {
                complete(taskService.getTask(taskId))
              } ~
                delete {
                  complete(taskService.deleteTask(taskId))
                }
            }
          } ~
            pathPrefix("tasks") {
              pathEndOrSingleSlash {
                post {
                  entity(as[Problems]) { problem =>
                    complete(taskService.addNewTask(problem.problem))
                  }
                }
              }
            } ~
            pathPrefix("solutions" / Segment) { taskId =>
              pathEndOrSingleSlash {
                get {
                  complete(solutionService.getSolutions(taskId))
                }
              }
            } ~
            pathPrefix("admin") {
              authorize(authService.hasAdminPermissions(user)) {
                pathPrefix("update-status" / Segment) { taskId =>
                  pathEndOrSingleSlash {
                    patch {
                      entity(as[Status]) { status =>
                        complete(taskService.updateTask(taskId, status))
                      }
                    }
                  }
                } ~
                  pathPrefix("tasks") {
                    pathEndOrSingleSlash {
                      get {
                        complete(taskService.getAllTask)
                      }
                    }
                  } ~
                  pathPrefix("shutdown") {
                    pathEndOrSingleSlash {
                      post {
                        complete {
                          system.terminate().map(_.getAddressTerminated()).value
                          "System is shutting down..."
                        }
                      }
                    }
                  } ~
                  pathPrefix("status") {
                    pathEndOrSingleSlash {
                      get {
                        complete(SystemStatus(system.name, system.startTime, system.uptime + " milliseconds"))
                      }
                    }
                  }
              }
            }
        }
      }
    }
  }
}
