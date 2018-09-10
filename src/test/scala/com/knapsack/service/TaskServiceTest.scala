package com.knapsack.service

import akka.actor.ActorSystem
import com.knapsack.model.{Problem, Status}
import org.scalatest.{AsyncFlatSpec, Matchers}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

class TaskServiceTest extends AsyncFlatSpec with Matchers {
  behavior of "the task service"

  private implicit val system: ActorSystem = ActorSystem("knapscak-service")
  private implicit val ec: ExecutionContextExecutor = system.dispatcher

  val problems = List(
    Problem(capacity = 60, weights = List(10, 20, 33), values = List(10, 3, 30)),
    Problem(capacity = 50, weights = List(10, 20, 30), values = List(60, 100, 120))
  )

  it should "add two problems and generate two tasks" in {
    val taskService = new TaskService()
    Future.sequence(problems.map(problem => taskService.addNewTask(problem))) map {
      tasks =>
        tasks.foreach(_.status shouldBe "Submitted")
        tasks should have size 2
    }
  }

  it should "return two tasks" in {
    val taskService = new TaskService()
    taskService.getAllTask map { tasks =>
      tasks.submitted should have size 0
    }
  }

  it should "update the status of a task" in {
    val taskService = new TaskService()
    val tasks = Await.result(Future.sequence(problems.map(problem => taskService.addNewTask(problem))), 5 seconds)
    val firstTask = tasks.head
    taskService.updateTask(firstTask.task, Status("Started")) map {
      case Some(t) => t.status shouldBe "Started"
    }
  }

  it should "delete the task successfully" in {
    val taskService = new TaskService()
    val tasks = Await.result(Future.sequence(problems.map(problem => taskService.addNewTask(problem))), 5 seconds)
    val firstTask = tasks.head
    taskService.deleteTask(firstTask.task) map (success => success shouldBe s"Task ${firstTask.task} is deleted.")
  }
}
