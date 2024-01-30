ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

val kafkaVersion = "3.4.0"

lazy val root = (project in file("."))
  .settings(
    name := "scala-kafka",
    resolvers += "Confluent Maven Repo" at "https://packages.confluent.io/maven/",
    libraryDependencies ++= Seq(
      "org.apache.kafka" % "kafka-clients" % kafkaVersion,
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.15.1"
    ),
    idePackagePrefix := Some("io.andrelucas"),
  )


//"org.apache.kafka" % "kafka-clients" % kafkaVersion,