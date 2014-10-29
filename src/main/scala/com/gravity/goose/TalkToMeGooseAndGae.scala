package com.gravity.goose

import java.io._
import scala.collection.JavaConversions._
import scala.io.Source

object TalkToMeGooseAndGae {
  import com.google.appengine.tools.development.testing.LocalURLFetchServiceTestConfig
  import com.google.appengine.tools.development.testing.LocalServiceTestHelper
  val URLConfig: LocalURLFetchServiceTestConfig = new LocalURLFetchServiceTestConfig()
  val Helper: LocalServiceTestHelper = new LocalServiceTestHelper(URLConfig)

  /**
   * You can use this method to run goose from the command line
   * to extract html from a bash script, or to just test its functionality:
   *
   *   cd into the goose root
   *   mvn compile
   *   MAVEN_OPTS="-Xms256m -Xmx2000m"; mvn exec:java -Dexec.mainClass=com.gravity.goose.TalkToMeGoose -Dexec.args="http://techcrunch.com/2011/05/13/native-apps-or-web-apps-particle-code-wants-you-to-do-both/" -e -q > ~/Desktop/gooseresult.txt
   *
   * or if using sbt:
   *
   *   cd into the goose root
   *   sbt
   *   > run http://www.thestar.com/news/insight/2013/04/26/spotting_tiny_gnatcatcher_can_put_a_spring_in_your_step.html
   *
   */
  def main(args: Array[String]) {
    try {
      println("URL to extract article from:")
      val url: String = if (args.isEmpty) readLine() else args(0)
      talk(url)
      //talk2(url)
    } catch {
      case e: Exception => {
        System.out.println("Make sure you pass in a valid URL: " + e.toString)
        e.printStackTrace()
      }
    } finally {
      Helper.tearDown()
    }
  }

  def talk(url: String) {
    import org.apache.log4j.BasicConfigurator
    Helper.setUp()
    BasicConfigurator.configure();

    val config: Configuration = new Configuration
    config.enableImageFetching = false

    val goose = new Goose()
    goose.setConfig(config)

    val article = goose.extractContent(url)
    println("Tags: " + article.getTags())
    println(article.cleanedArticleText)
  }
}
