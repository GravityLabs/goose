resolvers ++= Seq("snapshots" at "http://scala-tools.org/repo-snapshots",
                  "releases"  at "http://scala-tools.org/repo-releases", 
                  "umeng.com snapshots" at "http://122.11.52.227:8088/nexus/content/repositories/snapshots",
                  "umeng.com releases" at "http://122.11.52.227:8088/nexus/content/repositories/releases",
                  "Oracle Maven 2 Repository" at "http://download.oracle.com/maven", 
                  "JBoss Maven 2 Repository" at "http://repository.jboss.com/maven2",
                  "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
                  )

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.9.0")

addSbtPlugin("com.twitter" % "sbt-package-dist" % "1.1.0-SNAPSHOT")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.2")
