name := """play-scala-endpoint"""
organization := "com.barnwaldo"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

// scala dependencies
val akkaVersion = "2.6.0"
scalaVersion := "2.12.10"

libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.0"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.0"
libraryDependencies += "com.typesafe.akka" %% "akka-stream-typed" % "2.6.0"
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.6.0"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3" % Test

// play dependencies (guice, ws, bootstrap, etc.)
libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += "org.webjars" % "bootstrap" % "4.3.1"

// Jackson json
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.1"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.10.1"

// kafka dependencies
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.3.1"
libraryDependencies += "org.apache.kafka" % "kafka-streams" % "2.3.1"
libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "2.3.1"
libraryDependencies += "com.typesafe.akka" %% "akka-stream-kafka" % "1.1.0"

// test dependencies
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "4.0.1" % Test
libraryDependencies += "org.apache.kafka" % "kafka-streams-test-utils" % "2.3.1" % Test

// sbt run configuration dev settings
// PlayKeys.devSettings += "play.server.websocket.frame.maxLength" -> "64k"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-Xfatal-warnings"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.barnwaldo.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.barnwaldo.binders._"




