package com.knapsack.util

import com.typesafe.scalalogging.LazyLogging

import scala.util.Random

object Helper extends LazyLogging {
  def generateUuid(length: Int = 12): String = Random.alphanumeric.take(length).mkString
}
