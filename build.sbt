name := "goose"

version := "2.1.14"

scalaVersion := "2.9.1"

scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies += "org.jsoup" % "jsoup" % "1.5.2"

libraryDependencies += "log4j" % "log4j" % "1.2.16"

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.6.6"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.6.1"

libraryDependencies += "commons-io" % "commons-io" % "2.0.1"

libraryDependencies += "commons-lang" % "commons-lang" % "2.6"

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.1.3"

libraryDependencies += "com.novocode" % "junit-interface" % "0.8" % "test"
