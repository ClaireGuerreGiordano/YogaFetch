
name := "fetch-yoga"

version := "0.1"

scalaVersion := "2.13.6"

organization := "yoga"

libraryDependencies ++= Seq(fetch,
  cats)

mainClass := Some("yoga.fetch.YogaApp")



val fetch = "com.47deg" %% "fetch" % "2.0.0"
val cats  = "org.typelevel" %% "cats-core" % "2.6.1"
