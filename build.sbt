import sbt._
import Keys._
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys
//assembly: import com.twitter.sbt._
//assembly: import AssemblyKeys._

//assembly: seq(StandardProject.newSettings: _*)

//organization := "GravityLabs"
organization := "com.gravity.goose"

name := "goose"

version := "2.2.0"

organizationHomepage := Some(url("http://gravity.com/"))

homepage := Some(url("https://github.com/GravityLabs/goose"))

description := "Extracts text, metadata, and key image from web articles."

licenses += "Apache2" -> url("http://www.apache.org/licenses/")

//scalaVersion := "2.11.1"
scalaVersion := "2.10.2"

crossScalaVersions := Seq("2.11.1", "2.11.0", "2.10.4")

testFrameworks += TestFrameworks.ScalaTest

testOptions in Test += Tests.Argument("-oF")

//assembly: seq(assemblySettings: _*)
resolvers ++= Seq(
  //"sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "central mvn repo" at "http://repo1.maven.org/",
  "Oracle Maven 2 Repository" at "http://download.oracle.com/maven",
  "JBoss Maven 2 Repository" at "http://repository.jboss.com/maven2",
  "JLangDetect Maven repository" at "https://jlangdetect.googlecode.com/svn/repo",
  "raisercostin repository" at "svn://raisercostin.synology.me/repo/maven/releases"
)

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

libraryDependencies ++= {
  Seq(
    "org.slf4j" % "slf4j-api" % "1.7.7"
    ,"commons-io" % "commons-io" % "2.4"
    ,"org.apache.httpcomponents" % "httpclient" % "4.3.3"
    ,"commons-lang" % "commons-lang" % "2.6"
    ,"com.ibm.icu" % "icu4j" % "53.1"
    ,"me.champeau.jlangdetect" % "jlangdetect-extra" % "0.4"
    ,"org.jsoup" % "jsoup" % "1.7.3"
    ,"net.liftweb" % "lift-json_2.10" % "2.5"
    ,"log4j" % "log4j" % "1.2.14"
    ,"com.typesafe" % "config" % "1.0.2"
    ,"com.jsuereth" %% "scala-arm" % "1.2"
    ,"org.specs2" %% "specs2" % "1.13"
    ,"org.jsoup" % "jsoup" % "1.7.3"
    ,"com.chenlb.mmseg4j" % "mmseg4j-core" % "1.9.1"
	,"com.googlecode.juniversalchardet" % "juniversalchardet" % "1.0.3"
	//gae
	,"com.google.appengine" % "appengine-api-labs" % "1.7.1"
	,"com.google.appengine" % "appengine-api-stubs" % "1.7.1"
	,"com.google.appengine" % "appengine-testing" % "1.7.1"
	,"com.google.appengine" % "appengine-api-1.0-sdk" % "1.7.1"
	//add json service
	,"com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2"
	,"com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.2.2"
	,"org.simpleframework" % "simple" % "4.1.21"
	//add cassandra
	//,"com.netflix.astyanax" % "astyanax-core" % "1.56.43"
	//,"com.netflix.astyanax" % "astyanax-thrift" % "1.56.43"
	//,"com.netflix.astyanax" % "astyanax-cassandra" % "1.56.43"
	//tests
	,"junit" % "junit" % "4.11" % "test"
    ,"org.scalatest" %% "scalatest" % "2.1.6" % "test"
    ,"org.slf4j" % "slf4j-log4j12" % "1.7.7" % "test"
    ,"com.novocode" % "junit-interface" % "0.10-M4" % "test"
    //"org.scala-lang" % "scala-compiler" % "2.9.0-1",
    //"org.scala-lang" % "scala-library" % "2.9.0-1",
    //"org.scala-lang" % "scala-reflect" % "2.10.0",
  )
}

publishMavenStyle := true

pomIncludeRepository := { _ => true}

//publishTo := Some(Resolver.file("Github Pages", Path.userHome /"repo" / "maven" asFile)(Patterns(true, Resolver.mavenStyleBasePattern)))
//publishTo := Some(Resolver.file("goose",  new File("d:/Dropbox/public/libs"))(Patterns(true, Resolver.mavenStyleBasePattern)) )
publishTo := Some(Resolver.file("goose",  new File("./target/publish"))(Patterns(true, Resolver.mavenStyleBasePattern)) )

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

// Get rid of java source directories in compile
unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_))

// Get rid of java source directories in test
unmanagedSourceDirectories in Test <<= (scalaSource in Test)(Seq(_))

//assembly: packageDistDir <<= (baseDirectory, packageDistName) { (b, n) => b / "release" }

parallelExecution in Test := false

net.virtualvoid.sbt.graph.Plugin.graphSettings


scalacOptions ++= Seq("-unchecked", "-deprecation")

//to see https://bitbucket.org/diversit/webdav4sbt
def svnPub = Command.args("svn", "<tag>") { (state, args) =>
    val svnUrl = """https://raisercostin.googlecode.com/svn/maven2"""
	val tag = s"""svn import -m "binary release" target\\publish\\ $svnUrl """
	println(s"\nexecute $tag")
	tag.!
	state
}

commands ++= Seq(svnPub)