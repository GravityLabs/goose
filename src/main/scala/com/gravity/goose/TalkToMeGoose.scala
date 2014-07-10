package com.gravity.goose

import java.io._
import scala.collection.JavaConversions._
import scala.io.Source


object TalkToMeGoose {
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
   * Some top gun love:
   * Officer: [in the midst of the MIG battle] Both Catapults are broken, sir.
   * Stinger: How long will it take?
   * Officer: It'll take ten minutes.
   * Stinger: Bullshit ten minutes! This thing will be over in two minutes! Get on it!
   *
   * @param args
   */
  def main(args: Array[String]) {
    try {
      talk(args(0))
    } catch {
      case e: Exception => {
        System.out.println("Make sure you pass in a valid URL: " + e.toString)
        e.printStackTrace()
      }
    }
  }

  def talk(url: String) {
    val config: Configuration = new Configuration
    config.enableImageFetching = false
    val goose = new Goose(config)
    val article = goose.extractContent(url)
    println("TITLE: " + article.title)
    println("DATE: " + article.publishDate)
    println("TAGS: " + article.tags)
    println("TEXT: " + article.cleanedArticleText)
  }
}
