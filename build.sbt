<<<<<<< HEAD
import sbt._
import Keys._

name := "goose-fork"

version := "2.1.23"

organization := "com.gravity.goose"

scalaVersion := "2.11.1"

crossScalaVersions := Seq("2.11.0", "2.10.4")
=======
import com.twitter.sbt._
import AssemblyKeys._

seq(StandardProject.newSettings: _*)



name := "goose"

version := "2.1.22"

organization := "com.gravity.goose"

scalaVersion := "2.10.1"
>>>>>>> remotes/kkme/master

testFrameworks += TestFrameworks.ScalaTest

testOptions in Test += Tests.Argument("-oF")

<<<<<<< HEAD
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

publishTo := Some(Resolver.file("Github Pages", Path.userHome /"repo" / "maven" asFile)(Patterns(true, Resolver.mavenStyleBasePattern)))
=======
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
    "commons-lang" % "commons-lang" % "2.6",
    "com.chenlb.mmseg4j" % "mmseg4j-core" % "1.9.1"
  )
}


packageDistDir <<= (baseDirectory, packageDistName) { (b, n) => b / "release" }

parallelExecution in Test := false



>>>>>>> remotes/kkme/master
