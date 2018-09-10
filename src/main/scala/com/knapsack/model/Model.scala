package com.knapsack.model

object Model {
  val statuses =
    Set(
      "SUBMITTED",
      "STARTED",
      "COMPLETED"
    )
}

case class Problems(problem: Problem)

case class Problem(capacity: Int, weights: List[Int], values: List[Int]) {
  require(capacity > 0, "Capacity should be a positive integer.")
  require(weights.nonEmpty, "Weights should not be empty.")
  require(weights.exists(v => v > 0), "Weights should have positive integer value.")
  require(values.length == weights.length, "Weights and values should have same size.")
  require(values.exists(v => v > 0), "Values should have positive integer value.")

  override def toString = f"Problem | $capacity%13d | ${weights.mkString(",")}%15s | ${values.mkString(",")}%15s"
}

case class Task(task: String, status: String, timestamps: Timestamp) {
  require(task.nonEmpty, "Task should have some value.")
  require(Model.statuses.contains(status.toUpperCase), s"Status should be one of ${Model.statuses.mkString(",")}.")

  override def toString = f"Task | $task%10s | $status%10s | ${timestamps.submitted}%15s | ${timestamps.started}%15s | ${timestamps.completed}%15s"
}

case class Timestamp(submitted: String, started: Option[String], completed: Option[String])

case class Solution(task: String, problem: Problem, solution: SolutionItem)

case class SolutionItem(items: List[Int], time: Option[Long])

case class Tasks(submitted: List[Task], started: List[Task], completed: List[Task])

case class Status(status: String) {
  require(Model.statuses.contains(status.toUpperCase), s"Status should be one of ${Model.statuses.mkString(",")}.")
}

case class SystemStatus(name: String, startTime: Long, upTime: String)
