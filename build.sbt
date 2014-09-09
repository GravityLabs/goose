name := "goose"

organization := "com.gravity"

version := "2.2.1-SNAPSHOT"
 
scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "1.7.6",
  "org.slf4j" % "slf4j-log4j12" % "1.7.6",
  //"org.clapper" % "grizzled-slf4j_2.10" % "1.0.1"
  "org.jsoup" % "jsoup" % "1.5.2",
  "org.apache.httpcomponents" % "httpclient" % "4.1.2",
  "commons-io" % "commons-io" % "2.0.1",
  "commons-lang" % "commons-lang" % "2.6",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "junit" % "junit" % "4.8.1" % "test",
  "com.novocode" % "junit-interface" % "0.10" % "test"
)
