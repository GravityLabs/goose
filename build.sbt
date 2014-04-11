import com.twitter.sbt._
import AssemblyKeys._

seq(StandardProject.newSettings: _*)



name := "goose"

version := "2.1.22"

organization := "com.gravity.goose"

scalaVersion := "2.10.1"

testFrameworks += TestFrameworks.ScalaTest

testOptions in Test += Tests.Argument("-oF")

seq(assemblySettings: _*)

resolvers ++= Seq(
                  "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
                  "central mvn repo" at "http://repo1.maven.org/",
                  "Oracle Maven 2 Repository" at "http://download.oracle.com/maven", 
                  "JBoss Maven 2 Repository" at "http://repository.jboss.com/maven2"
                  )

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

libraryDependencies ++= {
  Seq(
    "net.liftweb" % "lift-json_2.10" % "2.5",
    "log4j" % "log4j" % "1.2.14",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "org.scalatest" %% "scalatest" % "1.9.1" % "test",
    "com.typesafe" % "config" % "1.0.2",
    "org.scala-lang" % "scala-reflect" % "2.10.0",
    "com.jsuereth" %% "scala-arm" % "1.2" ,
    "org.specs2" %% "specs2" % "1.13",
    "junit" % "junit" % "4.8.1" % "test",
    "org.slf4j" % "slf4j-api" % "1.6.1",
    "org.slf4j" % "slf4j-log4j12" % "1.6.1" % "test",
    "org.jsoup" % "jsoup" % "1.5.2",
    "commons-io" % "commons-io" % "2.0.1",
    "org.scala-lang" % "scala-compiler" % "2.9.0-1",
    "org.scala-lang" % "scala-library" % "2.9.0-1",
    "org.apache.httpcomponents" % "httpclient" % "4.1.2",
    "commons-lang" % "commons-lang" % "2.6"
  )
}


packageDistDir <<= (baseDirectory, packageDistName) { (b, n) => b / "release" }

parallelExecution in Test := false



