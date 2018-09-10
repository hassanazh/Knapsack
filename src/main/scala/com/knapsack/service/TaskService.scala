package com.knapsack.service

import com.knapsack.model._
import com.knapsack.util.Helper
import com.typesafe.scalalogging.LazyLogging

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}

class TaskService(implicit val ec: ExecutionContext) extends LazyLogging {

  private val taskList: ListBuffer[Task] = new ListBuffer[Task]
  private val taskProblem: ListBuffer[(String, Problem)] = new ListBuffer[(String, Problem)]

  def getTask(taskId: String): Future[Option[Task]] = {
    Future(taskList.find(_.task == taskId))
  }

  def addNewTask(problem: Problem): Future[Task] = {
    logger.debug("New problem received: " + problem.toString)

    val taskId = Helper.generateUuid()
    taskProblem.append((taskId, problem))

    val timeStamp = Timestamp(System.currentTimeMillis().toString, None, None)
    val task = Task(task = taskId, status = "Submitted", timestamps = timeStamp)
    taskList.append(task)

    logger.debug("New task added: " + task.toString)
    Future(task)
  }

  def getAllTask: Future[Tasks] = {
    Future(Tasks(taskList.filter(_.status.equalsIgnoreCase("Submitted")).toList,
      taskList.filter(_.status.equalsIgnoreCase("Started")).toList,
      taskList.filter(_.status.equalsIgnoreCase("Completed")).toList
    ))
  }

  def getAllTaskProblem: Map[String, Problem] = taskProblem.toMap

  def deleteTask(taskId: String): Future[String] = {
    Future(taskList.find(_.task == taskId).map { t =>
      taskList.remove(taskList.indexWhere(_.task == taskId))

      logger.debug("Task deleted: " + t.toString)
      s"Task $taskId is deleted."
    }.getOrElse(s"Task $taskId not found."))
  }

  def updateTask(taskId: String, status: Status): Future[Option[Task]] = {
    getTask(taskId).flatMap { task =>
      task.map{ t =>
        val timestamp = if(status.status.equalsIgnoreCase("Completed")) {
          t.timestamps.copy(completed = Some(System.currentTimeMillis().toString))
        } else if(status.status.equalsIgnoreCase("Started")){
          t.timestamps.copy(started = Some(System.currentTimeMillis().toString))
        } else {
          throw new IllegalArgumentException(s"${status.status} is not possible.")
        }
        val updatedTask = t.copy(status = status.status, timestamps = timestamp)

        deleteTask(taskId).map(_ => taskList.append(updatedTask)).map(_ => updatedTask)
      } match {
        case Some(value)  => value.map(Some(_))
        case _ => Future(None)
      }
    }
  }
}
