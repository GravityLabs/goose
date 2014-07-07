resolvers ++= Seq(
	//"releases"  at "http://scala-tools.org/repo-releases", 
	//"umeng.com releases" at "http://122.11.52.227:8088/nexus/content/repositories/releases",
	"Oracle Maven 2 Repository" at "http://download.oracle.com/maven", 
	"JBoss Maven 2 Repository" at "http://repository.jboss.com/maven2",
	"sbt-plugin-releases" at "http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases"
)

//addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.2")

//addSbtPlugin("com.twitter" % "sbt-package-dist" % "1.0.6")

//addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.2.1")
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.3.0")
