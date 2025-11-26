name := "imagevault-service"
organization := "com.hotel.booking"
version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.16"

libraryDependencies += guice
libraryDependencies += filters

libraryDependencies ++= Seq(
  // Slick + MySQL
  "org.playframework" %% "play-slick"            % "6.1.0",
  "org.playframework" %% "play-slick-evolutions" % "6.1.0",
  "mysql" % "mysql-connector-java" % "8.0.26",

  // JSON
  "com.typesafe.play" %% "play-json" % "2.9.4",

  // Test
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test
)
