import sbt._
import Keys._


object GooseBuild extends Build {

    lazy val goose = Project(
        id = "goose",
        base = file("."),
        settings = Project.defaultSettings ++ Seq(
            description := "Html Content / Article Extractor in Scala",
            organization := "com.gravity",
            version := "2.1.22-SNAPSHOT",
            version <<= version { v => //only release *if* -Drelease=true is passed to JVM
                val release = Option(System.getProperty("release")) == Some("true")
                if (release) {
                    v
                } else {
                    val suffix = Option(System.getProperty("suffix"))
                    val i = (v.indexOf('-'), v.length) match {
                        case (x, l) if x < 0 => l
                        case (x, l) if v substring (x + 1) matches """\d+""" => l //patch level, not RCx
                        case (x, _) => x
                    }
                    v.substring(0, i) + "-" + (suffix getOrElse "SNAPSHOT")
                }
            },
            parallelExecution := false,
            publishMavenStyle := true,
            scalaVersion := "2.9.2",
            crossScalaVersions := Seq("2.9.2", "2.9.1", "2.9.0"),
            licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
            homepage := Some(url("http://github.com/jaytaylor/goose")),
            pomExtra := (
                <scm>
                    <url>git@github.com:jaytaylor/goose.git</url>
                    <connection>scm:git:git@github.com:jaytaylor/goose.git</connection>
                </scm>
                <developers>
                    <developer>
                        <id>jaytaylor</id>
                        <name>Jay Taylor</name>
                        <url>https://github.com/jaytaylor</url>
                    </developer>
                </developers>
            ),
            publishTo <<= version { v =>
                Some(Resolver.sftp(
                    "Scala.sh Repository",
                    "scala.sh",
                    "/var/www/scala.sh/public_html/repositories/" + (
                        if (v.trim.endsWith("SNAPSHOT")) { "snapshots" } else { "releases" }
                    )
                ))
            },
            publishArtifact in Test := false,
            pomIncludeRepository := { _ => false },
            resolvers ++= Seq(
                "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases",
                "JBoss Repository" at "http://repository.jboss.org/nexus/content/groups/public",
                "CodaHale Repository" at "http://repo.codahale.com",
                "Scala.sh Releases" at "http://scala.sh/repositories/releases",
                "Scala.sh Snapshots" at "http://scala.sh/repositories/snapshots",
                "Maven1" at "http://repo1.maven.org/maven2",
                "Typesafe Artifactory" at "http://typesafe.artifactoryonline.com/typesafe/repo",
                "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
                "iBiblio Maven2" at "http://mirrors.ibiblio.org/maven2"
            ),
            libraryDependencies ++= Seq(
                "junit" % "junit" % "4.8.1",
                "org.slf4j" % "slf4j-api" % "1.6.1",
                "org.slf4j" % "slf4j-log4j12" % "1.6.1",
                "org.jsoup" % "jsoup" % "1.5.2",
                "commons-io" % "commons-io" % "2.0.1",
                "org.apache.httpcomponents" % "httpclient" % "4.1.2",
                "commons-lang" % "commons-lang" % "2.6"
            ),
            libraryDependencies <++= scalaVersion { sv =>
                Seq(
                    "org.scala-lang" % "scalap" % sv,
                    if (sv startsWith "2.9") {
                        "org.scalatest" % "scalatest_2.9.1" % "1.6.1" % "test"
                    } else {
                        "org.scalatest" % "scalatest_2.8.2" % "1.5.1" % "test"
                    }
                )
            }
        )
    )
}

