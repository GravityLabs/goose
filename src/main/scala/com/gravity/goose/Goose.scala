package com.gravity.goose

import cleaners.{DocumentCleaner, StandardDocumentCleaner}
import extractors.{ContentExtractor, StandardContentExtractor}
import network.HtmlFetcher
import utils.{Logging, URLHelper}
import org.jsoup.Jsoup
import org.jsoup.nodes.{Element, Document}
import org.apache.http.client.HttpClient

/**
 * Created by Jim Plush - Gravity.com
 * Date: 8/14/11
 */


class Goose extends Logging {

  import Goose._


  def extractContent(url: String)(implicit config: Configuration): Article = {

    for {
      pc <- URLHelper.getCleanedUrl(url)
      rawHtml <- HtmlFetcher.getHtml(pc.url.toString)
      doc <- getDocument(pc.url.toString, rawHtml)
    } {
      trace("Crawling url: %s".format(pc.url))

      val extractor = getExtractor
      val docCleaner = getDocCleaner

      val article = new Article()
      article.finalUrl = pc.url.toString
      article.rawHtml = rawHtml
      article.doc = doc
      article.rawDoc = doc

      article.title = extractor.getTitle(article)
      article.metaDescription = extractor.getMetaDescription(article)
      article.metaKeywords = extractor.getMetaKeywords(article)
      article.canonicalLink = extractor.getCanonicalLink(article)
      article.domain = extractor.getDomain(article.finalUrl)

      // before we do any calcs on the body itself let's clean up the document
      article.doc = docCleaner.clean(article)

      extractor.calculateBestNodeBasedOnClustering(article) match {
        case Some(node: Element) => {
          article.topNode = node
          article.movies = extractor.extractVideos(article.topNode)
          article.topNode = extractor.postExtractionCleanup(article.topNode)
          return article



          //          if (config.isEnableImageFetching) {
          //            var httpClient: HttpClient = HtmlFetcher.getHttpClient
          //            imageExtractor = getImageExtractor(httpClient, urlToCrawl)
          //            article.setTopImage(imageExtractor.getBestImage(doc, article.getTopNode))
          //          }
        }
        case _ => trace("NO ARTICLE FOUND");return null
      }
      return article
    }
    null
  }

  def getDocCleaner: DocumentCleaner = {
    new StandardDocumentCleaner
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
  val config: Configuration = new Configuration
}