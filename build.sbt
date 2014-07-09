name := "goose"

version := "2.11.0"

organization := "com.sapienapps"

scalaVersion := "2.11.1"

//resolvers += "Typesafe Maven Repository" at "http://repo.typesafe.com/typesafe/maven-releases/"

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.8.1",
  "org.slf4j" % "slf4j-api" % "1.6.1",
  "org.jsoup" % "jsoup" % "1.5.2",
  "commons-io" % "commons-io" % "2.0.1",
  "org.apache.httpcomponents" % "httpclient" % "4.1.2",
  "commons-lang" % "commons-lang" % "2.6"
)

publishTo := Some(Resolver.file("goose",  new File(Path.userHome + "/Dropbox/public/libs" )) )
    