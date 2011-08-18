package com.gravity.goose

import cleaners.{DocumentCleaner, StandardDocumentCleaner}
import extractors.{ContentExtractor, StandardContentExtractor}
import images.{ImageExtractor, StandardImageExtractor}
import network.HtmlFetcher
import outputformatters.{StandardOutputFormatter, OutputFormatter}
import utils.{Logging, URLHelper}
import org.jsoup.Jsoup
import org.jsoup.nodes.{Element, Document}
import org.apache.http.client.HttpClient
import java.io.File

/**
 * Created by Jim Plush - Gravity.com
 * Date: 8/14/11
 */


class Goose()(implicit config: Configuration) extends Logging {

  import Goose._

  initializeEnvironment()


  def extractContent(url: String): Article = {


    for {
      pc <- URLHelper.getCleanedUrl(url)
      rawHtml <- HtmlFetcher.getHtml(pc.url.toString)
      doc <- getDocument(pc.url.toString, rawHtml)
    } {
      trace("Crawling url: %s".format(pc.url))

      val extractor = getExtractor
      val docCleaner = getDocCleaner
      val outputFormatter = getOutputFormatter

      val article = new Article()
      article.finalUrl = pc.url.toString
      article.linkhash = pc.linkhash
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
          article.cleanedArticleText = outputFormatter.getFormattedText(article.topNode)

          if (config.enableImageFetching) {
            val imageExtractor = getImageExtractor(article)
            article.topImage = imageExtractor.getBestImage(doc, article.topNode)
          }
          return article
        }
        case _ => trace("NO ARTICLE FOUND"); return null
      }
      return article
    }
    null
  }

  def getImageExtractor(article: Article): ImageExtractor = {
    val httpClient: HttpClient = HtmlFetcher.getHttpClient
    new StandardImageExtractor(httpClient, article, config)
  }

  def getOutputFormatter: OutputFormatter = {
    new StandardOutputFormatter
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
  }

}

object Goose {

  implicit val config = new Configuration
}