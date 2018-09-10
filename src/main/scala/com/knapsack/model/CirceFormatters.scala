package com.knapsack.model

import io.circe.java8.time.TimeInstances

object CirceFormatters extends TimeInstances {

  import io.circe._
  import io.circe.generic.semiauto._

  implicit val encoderProblems: Encoder[Problems] = deriveEncoder
  implicit val decoderProblems: Decoder[Problems] = deriveDecoder
  implicit val encoderProblem: Encoder[Problem] = deriveEncoder
  implicit val decoderProblem: Decoder[Problem] = deriveDecoder
  implicit val encoderTask: Encoder[Task] = deriveEncoder
  implicit val decoderTask: Decoder[Task] = deriveDecoder
  implicit val encoderTasks: Encoder[Tasks] = deriveEncoder
  implicit val decoderTasks: Decoder[Tasks] = deriveDecoder
  implicit val encoderTimestamp: Encoder[Timestamp] = deriveEncoder
  implicit val decoderTimestamp: Decoder[Timestamp] = deriveDecoder
  implicit val encoderSolutionItem: Encoder[SolutionItem] = deriveEncoder
  implicit val decoderSolutionItem: Decoder[SolutionItem] = deriveDecoder
  implicit val encoderSolution: Encoder[Solution] = deriveEncoder
  implicit val decoderSolution: Decoder[Solution] = deriveDecoder
  implicit val encoderStatus: Encoder[Status] = deriveEncoder
  implicit val decoderStatus: Decoder[Status] = deriveDecoder
  implicit val encoderSystemStatus: Encoder[SystemStatus] = deriveEncoder
  implicit val decoderSystemStatus: Decoder[SystemStatus] = deriveDecoder
}
