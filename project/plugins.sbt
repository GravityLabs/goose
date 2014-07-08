resolvers ++= Seq(
	//"releases"  at "http://scala-tools.org/repo-releases", 
	//"umeng.com releases" at "http://122.11.52.227:8088/nexus/content/repositories/releases",
	"Oracle Maven 2 Repository" at "http://download.oracle.com/maven", 
	"JBoss Maven 2 Repository" at "http://repository.jboss.com/maven2",
	"sbt-plugin-releases" at "http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases",
    "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)

//addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.2")

//addSbtPlugin("com.twitter" % "sbt-package-dist" % "1.0.6")

//addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.2.1")
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.3.0")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.4")

// Comment to get more information during initialization
logLevel := Level.Warn

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.1") 