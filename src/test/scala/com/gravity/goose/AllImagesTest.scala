package com.gravity.goose

import extractors.PublishDateExtractor
import org.junit.Test
import org.junit.Assert._
import utils.FileHelper
import java.text.SimpleDateFormat
import org.jsoup.select.Selector
import org.jsoup.nodes.Element
import java.util.Date

/**
 * Created by Francisco Vieira
 * User: fvieira
 * Date: 27/10/14
 */

class AllImagesTest {

  def getHtml(filename: String): String = {
    FileHelper.loadResourceFile(TestUtils.staticHtmlDir + filename, Goose.getClass)
  }

  @Test
  def allImages() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url = "http://blog.pkhamre.com/2012/07/24/understanding-statsd-and-graphite/"
    val html = getHtml("allImages.txt")
    val article = TestUtils.getArticle(url, html)
    val images = "http://blog.pkhamre.com/images/irssi-conversation.png" ::
        "http://blog.pkhamre.com/images/graphite-render.png" ::
        "http://blog.pkhamre.com/images/graphite-registrations.png" ::
        "http://blog.pkhamre.com/images/graphite-registrations-derivative.png" ::
        Nil
    TestUtils.runArticleAssertions(article = article, expectedImages = images)
    TestUtils.printReport()
  }

}
