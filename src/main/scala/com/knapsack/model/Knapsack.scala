package com.knapsack.model

import com.typesafe.scalalogging.LazyLogging

class Knapsack extends LazyLogging {
  case class WorthWeight(worth: Double, weight: Int)

  private def calculateWorthOfEachItem(capacity: Int, weight: List[Int], value: List[Int], index: Int = 0): Map[Int, WorthWeight] = {
    (weight, value) match {
      case (currentWeight :: remainingWeight, currentValue :: remainingValue) =>
        calculateWorthOfEachItem(capacity, remainingWeight, remainingValue, index + 1) + (index -> WorthWeight(currentValue.toDouble / currentWeight, currentWeight))
      case _ => Map.empty
    }
  }

  def calculateKnapsack(capacity: Int, weights: List[Int], values: List[Int]): List[Int] = {
    logger.info(f"Calculating knapsack with capacity: $capacity, weight: [${weights.mkString(",")}] and value: [${values.mkString(",")}]")
    getIndexesOfWeights(calculateWorthOfEachItem(capacity, weights, values), capacity)
  }

 private  def getIndexesOfWeights(worthListWithWeight: Map[Int, WorthWeight], capacity: Int): List[Int] = {
   worthListWithWeight.toList.sortWith((i1, i2) => i1._2.worth > i2._2.worth) match {
     case (index, worthWeight) :: remaining if worthWeight.weight <= capacity =>
       List(index) ++ getIndexesOfWeights(remaining.toMap, capacity - worthWeight.weight)
     case _ => Nil
   }
  }
}
