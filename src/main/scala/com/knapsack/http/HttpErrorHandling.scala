package com.knapsack.http

import java.util.concurrent.{TimeoutException => JTimeoutException}

import akka.http.scaladsl.model.{IllegalUriException, StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.pattern.AskTimeoutException
import com.typesafe.scalalogging.LazyLogging
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

import scala.concurrent.{TimeoutException => ScTimeoutException}
import scala.util.control.NonFatal

trait HttpErrorHandling {
  self: LazyLogging =>

  import StatusCodes._
  import io.circe._
  import io.circe.generic.semiauto._

  implicit val encoderErrorResponseEntity: Encoder[ErrorResponseEntity] = deriveEncoder

  object ErrorResponse {
    def apply(statusCode: StatusCode, message: String): (StatusCode, ErrorResponseEntity) = {
      (statusCode, ErrorResponseEntity(statusCode.intValue(), statusCode.reason(), message))
    }
  }
  case class ErrorResponseEntity(code: Int, reason: String, message: String = "")

  def exceptionHandler: ExceptionHandler = ExceptionHandler {
    case e: HttpAwareException =>
      val sc = StatusCodes.getForKey(e.httpStatus) getOrElse {
        throw new IllegalArgumentException(s"Non-standard http status code `${e.httpStatus}` received as a result")
      }
      logger.warn(e.toString)
      complete(ErrorResponse(sc, e.getMessage))
    case e@(_: AskTimeoutException | _: ScTimeoutException | _: JTimeoutException) =>
      logger.error("Request timed out", e)
      complete(ErrorResponse(GatewayTimeout, "Request timed out, please try again later. Cause: " + e.toString))
    case e: Throwable if e.getCause != null && e.getCause.isInstanceOf[IllegalArgumentException] =>
      logger.warn(e.getCause.toString)
      complete(ErrorResponse(BadRequest, e.getCause.getMessage))
    case e@(_: IllegalArgumentException | _: IllegalUriException) =>
      logger.warn(e.toString)
      complete(ErrorResponse(BadRequest, e.getMessage))
    case e: java.io.FileNotFoundException =>
      complete(ErrorResponse(NotFound, e.toString))
    case e: java.io.IOException =>
      logger.error("Service unavailable", e)
      complete(ErrorResponse(ServiceUnavailable, e.toString))
    case NonFatal(e) =>
      logger.error("Internal error", e)
      complete(ErrorResponse(InternalServerError, e.toString))
  }
}
