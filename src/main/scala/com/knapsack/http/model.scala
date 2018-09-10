package com.knapsack.http

class HttpAwareException(val httpStatus: Int, msg: String, cause: Throwable = null) extends Exception(msg, cause)

class BadRequestException(msg: String, cause: Throwable = null) extends HttpAwareException(400, msg, cause)
class UnauthorizedException(msg: String, cause: Throwable = null) extends HttpAwareException(401, msg, cause)
class ForbiddenException(msg: String, cause: Throwable = null) extends HttpAwareException(403, msg, cause)
class NotFoundException(msg: String, cause: Throwable = null) extends HttpAwareException(404, msg, cause)
class ConflictException(msg: String, cause: Throwable = null) extends HttpAwareException(409, msg, cause)
class PreconditionFailedException(msg: String, cause: Throwable = null) extends HttpAwareException(412, msg, cause)
class TooManyRequestException(msg: String, cause: Throwable = null) extends HttpAwareException(429, msg, cause)
class InternalServerException(msg: String, cause: Throwable = null) extends HttpAwareException(500, msg, cause)