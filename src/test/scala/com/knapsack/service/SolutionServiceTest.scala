package com.knapsack.service

import akka.actor.ActorSystem
import com.knapsack.model.{Knapsack, Problem}
import org.scalatest.{AsyncFlatSpec, Matchers}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

class SolutionServiceTest extends AsyncFlatSpec with Matchers {
  behavior of "the solution service"

  private implicit val system: ActorSystem = ActorSystem("knapscak-service")
  private implicit val ec: ExecutionContextExecutor = system.dispatcher

  val problems = List(
    Problem(capacity = 60, weights = List(10, 20, 33), values = List(10, 3, 30)),
    Problem(capacity = 70, weights = List(10, 20, 30, 40), values = List(60, 100, 120, 190))
  )

  it should "return a solution" in {
    val taskService = new TaskService()
    val solutionService = new SolutionService(taskService = taskService, new Knapsack())
    val tasks = Await.result(Future.sequence(problems.map(problem => taskService.addNewTask(problem))), 5 seconds)

    val firstTask = tasks.head
    val secondTask = tasks.last

    solutionService.getSolutions(firstTask.task) map {
      case Some(solution) =>
        solution.solution.items should contain (0)
        solution.solution.items should contain (2)
        solution.solution.items should have size 2
    }

    solutionService.getSolutions(secondTask.task) map {
      case Some(solution) =>
        solution.solution.items should contain (0)
        solution.solution.items should contain (1)
        solution.solution.items should contain (3)
        solution.solution.items should have size 3
    }
  }
}
