val akkaVersion                   = "2.5.14"
val akkaHttpVersion               = "10.1.3"
val akkaHttpCirceVersion          = "1.21.0"

val logbackVersion                = "1.2.3"

val circeVersion                  = "0.9.3"
val circeAkkaHttpVersion          = "1.21.0"

val slf4jVersion                  = "1.7.25"
val scalaTestVersion              = "3.0.4"
val scalaLoggingVersion           = "3.7.2"

val enumeratumVersion             = "1.5.12"
val enumeratumCirceVersion        = "1.5.15"

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % slf4jVersion,

  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceVersion,

  "com.fasterxml.uuid" % "java-uuid-generator" % "3.1.4",
  "com.beachape" %% "enumeratum" % enumeratumVersion,
  "com.beachape" %% "enumeratum-circe" % enumeratumCirceVersion,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-java8" % circeVersion,

  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",

  "org.slf4j" % "slf4j-api" % slf4jVersion,
  "org.slf4j" % "slf4j-jdk14" % slf4jVersion,
  "org.slf4j" % "jcl-over-slf4j" % slf4jVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion % "test"
)