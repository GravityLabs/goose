package com.gravity.goose

import network.HtmlFetcher
import utils.{Logging, URLHelper}

/**
 * Created by Jim Plush - Gravity.com
 * Date: 8/14/11
 */


class Goose extends Logging {

  import Goose._

  def extractContent(url: String): Article = {

    URLHelper.getCleanedUrl(url) match {
      case Some(parseCandidate) => {

        trace("Crawling url: %s".format(parseCandidate.url))
        println(HtmlFetcher.getHtml(parseCandidate.url.toString))

        val a = new Article
        a
      }
      case _ => {
        null
      }
    }


  }
}

object Goose {

}