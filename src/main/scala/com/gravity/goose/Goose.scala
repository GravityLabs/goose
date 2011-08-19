package com.gravity.goose

import utils.{Logging}
import java.io.File
import akka.actor.Actor

/**
 * Created by Jim Plush - Gravity.com
 * Date: 8/14/11
 */
class Goose()(implicit config: Configuration) extends Logging {

  import Goose._

  initializeEnvironment()

  /**
  * Main method to extract an article object from a URL, pass in a url and get back a Article
  * @url The url that you want to extract
  */
  def extractContent(url: String): Article = {

    val cc = new CrawlCandidate(config, url)
    val result = crawlingActor !! cc
    result match {
      case Some(article) => {
        article.asInstanceOf[Article]
      }
      case _ => null
    }
  }

  def initializeEnvironment() {

    val f = new File(config.localStoragePath)
    try {
      if (!f.isDirectory) {
        f.mkdirs()
      }
    } catch {
      case e: Exception =>
    }
    if (!f.isDirectory) {
      throw new Exception(config.localStoragePath + " directory does not seem to exist, you need to set this for image processing downloads")
    }
    if (!f.canWrite) {
      throw new Exception(config.localStoragePath + " directory is not writeble, you need to set this for image processing downloads")
    }

    // todo cleanup any jank that may be in the tmp folder currently
  }

}

object Goose {

  implicit val config = new Configuration

  val logPrefix = "goose: "

  // create the crawling actor that will accept bulk crawls
  val crawlingActor = Actor.actorOf[CrawlingActor]
  crawlingActor.start()

}