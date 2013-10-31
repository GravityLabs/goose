import sbt._ 
import Keys._

object Dependencies {
  val Slf4jApi = "org.slf4j" % "slf4j-api" % "1.6.6" 
  val Slf4jLog4j12 = "org.slf4j" % "slf4j-log4j12" % "1.6.6" 
  val Jsoup = "org.jsoup" % "jsoup" % "1.7.2" 
  val CommonsIo = "commons-io" % "commons-io" % "2.0.1" 
  val ScalaCompiler = "org.scala-lang" % "scala-compiler" % "2.9.2" 
  val ScalaLibrary = "org.scala-lang" % "scala-library" % "2.9.2" 
  val Httpclient = "org.apache.httpcomponents" % "httpclient" % "4.2.4" 
  val CommonsLang = "commons-lang" % "commons-lang" % "2.6" 
  val Juniversalchardet = "com.googlecode.juniversalchardet" % "juniversalchardet" % "1.0.3"

}

object GooseBuild extends Build {
  import Dependencies._

  lazy val root = Project("goose", file("."), 
                    settings = Defaults.defaultSettings ++ 
                    Seq(libraryDependencies ++= Seq(Slf4jApi, Slf4jLog4j12, Jsoup, CommonsIo, ScalaCompiler, ScalaLibrary, Httpclient, CommonsLang, Juniversalchardet)))
}