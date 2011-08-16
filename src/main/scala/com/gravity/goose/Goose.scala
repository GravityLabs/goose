package com.gravity.goose

import extractors.{ContentExtractor, StandardContentExtractor}
import network.HtmlFetcher
import utils.{Logging, URLHelper}
import org.jsoup.nodes.Document
import org.jsoup.Jsoup

/**
 * Created by Jim Plush - Gravity.com
 * Date: 8/14/11
 */


class Goose extends Logging {

  import Goose._

  def extractContent(url: String): Article = {

    for {
      pc <- URLHelper.getCleanedUrl(url)
      rawHtml <- HtmlFetcher.getHtml(pc.url.toString)
      doc <- getDocument(pc.url.toString, rawHtml)
    } {
      trace("Crawling url: %s".format(pc.url))

      val extractor = getExtractor

      val article = new Article()
      article.finalUrl = pc.url.toString
      article.rawHtml = rawHtml
      article.doc = doc
      article.rawDoc = doc


      article.title = extractor.getTitle(article)
      article.metaDescription = extractor.getMetaDescription(article)
      article.metaKeywords = extractor.getMetaKeywords(article)
      article.canonicalLink = extractor.getCanonicalLink(article)
      println("LINK: " + article.finalUrl)
      println("CANON: " + article.canonicalLink)

      return article
    }
    null
  }

  def getDocument(url: String, rawlHtml: String): Option[Document] = {

    try {
      Some(Jsoup.parse(rawlHtml))
    } catch {
      case e: Exception => {
        trace("Unable to parse %s properly into JSoup Doc".format(url))
        None
      }
    }


  }

  def getExtractor: ContentExtractor = {
    new StandardContentExtractor
  }
}

object Goose {

}