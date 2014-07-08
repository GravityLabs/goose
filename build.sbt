import sbt._
import Keys._

name := "goose-fork"

version := "2.1.23"

organization := "com.gravity.goose"

scalaVersion := "2.11.1"

crossScalaVersions := Seq("2.11.0", "2.10.4")

testFrameworks += TestFrameworks.ScalaTest

testOptions in Test += Tests.Argument("-oF")

resolvers ++= Seq(
  "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "central mvn repo" at "http://repo1.maven.org/",
  "Oracle Maven 2 Repository" at "http://download.oracle.com/maven",
  "JBoss Maven 2 Repository" at "http://repository.jboss.com/maven2",
  "JLangDetect Maven repository" at "https://jlangdetect.googlecode.com/svn/repo"
)

libraryDependencies ++= {
  Seq(
    "org.scalatest" %% "scalatest" % "2.1.6" % "test",
    "junit" % "junit" % "4.8.1" % "test",
    "org.slf4j" % "slf4j-api" % "1.7.7",
    "org.slf4j" % "slf4j-log4j12" % "1.7.7" % "test",
    "commons-io" % "commons-io" % "2.4",
    "org.apache.httpcomponents" % "httpclient" % "4.3.1",
    "commons-lang" % "commons-lang" % "2.6",
    "com.ibm.icu" % "icu4j" % "53.1",
    "com.novocode" % "junit-interface" % "0.10-M4" % "test",
    "me.champeau.jlangdetect" % "jlangdetect-extra" % "0.4",
    "org.jsoup" % "jsoup" % "1.7.3"
  )
}

publishMavenStyle := true

pomIncludeRepository := { _ => true}

publishTo := Some(Resolver.file("Github Pages", Path.userHome /"repo" / "maven" asFile)(Patterns(true, Resolver.mavenStyleBasePattern)))