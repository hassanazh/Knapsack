package com.knapsack.service

import com.knapsack.model.{Knapsack, Solution, SolutionItem}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}

class SolutionService(taskService: TaskService,  knapsack: Knapsack)(implicit val ec: ExecutionContext) extends LazyLogging {

  def getSolutions(taskId: String): Future[Option[Solution]] = {
    taskService.getAllTaskProblem.get(taskId) match {
      case Some(problem) =>
        val weightsToPut = knapsack.calculateKnapsack(problem.capacity, problem.weights, problem.values)
        taskService.getTask(taskId).map(task => task.map{ t =>
            val time = if(t.status.equalsIgnoreCase("Completed")) {
              t.timestamps.completed.map(t1 => System.currentTimeMillis() - t1.toLong)
            } else {
              None
            }
            Solution(taskId, problem, SolutionItem(weightsToPut, time))
        })
      case _ => Future(None)
    }
  }
}