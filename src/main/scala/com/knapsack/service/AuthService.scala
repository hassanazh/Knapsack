package com.knapsack.service

import akka.http.scaladsl.server.directives.Credentials
import com.typesafe.scalalogging.LazyLogging

class AuthService() extends LazyLogging {

  case class User(name: String)

  def myUserPassAuthenticator(credentials: Credentials): Option[User] =
    credentials match {
      case p @ Credentials.Provided(id) if p.verify("password")=> Some(User(id))
      case _                        => None
    }

  // check if user is authorized to perform admin actions:
  val admins = Set("authentic-admin")
  def hasAdminPermissions(user: User): Boolean =
    admins.contains(user.name)
}
