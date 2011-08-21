package com.gravity.goose

/**
 * Created by Jim Plush
 * User: jim
 * Date: 5/13/11
 */
object TalkToMeGoose {
  /**
  * you can use this method if you want to run goose from the command line to extract html from a bashscript
  * or to just test it's functionality
  * you can run it like so
  * cd into the goose root
  * mvn compile
  * MAVEN_OPTS="-Xms256m -Xmx2000m"; mvn exec:java -Dexec.mainClass=com.gravity.goose.TalkToMeGoose -Dexec.args="http://techcrunch.com/2011/05/13/native-apps-or-web-apps-particle-code-wants-you-to-do-both/" -e -q > ~/Desktop/gooseresult.txt
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
      val url: String = args(0)
      val config: Configuration = new Configuration
      config.enableImageFetching = false
      val goose = new Goose(config)
      val article = goose.extractContent(url)
      println(article.cleanedArticleText)
    }
    catch {
      case e: Exception => {
        System.out.println("Make sure you pass in a valid URL: " + e.toString)
      }
    }
  }
}


