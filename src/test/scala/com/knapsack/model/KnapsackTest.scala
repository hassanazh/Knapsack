package com.knapsack.model

import org.scalatest.FlatSpec

class KnapsackTest extends FlatSpec {
  behavior of "the solution service"

  it should "have two items selected" in  {
    val knapsack = new Knapsack()
    val result = knapsack.calculateKnapsack(capacity = 60, weights = List(10, 20, 33), values = List(10, 3, 30))

    assertResult(2)(result.length)
    assertResult(List(0,2), "Result should have weights 0 and 2 selected")(result)
  }

  it should "have three items selected" in  {
    val knapsack = new Knapsack()
    val result = knapsack.calculateKnapsack(capacity = 70, weights = List(10, 20, 30, 40), values = List(60, 100, 120, 190))

    assertResult(3)(result.length)
    assertResult(List(0,1,3), "Result should have weights 0,1 and 3 selected")(result)
  }
}
