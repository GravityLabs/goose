package com.gravity.goose

import org.junit.Test
import utils.FileHelper

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/19/11
 */

class TextExtractions {

  val resDir = "/com/gravity/goose/statichtml/"

  @Test
  def cnn1() {
    implicit val config = TestUtils.NO_IMAGE_CONFIG
    val html = FileHelper.loadResourceFile(resDir + "cnn1.txt", Goose.getClass)
    val url = "http://www.cnn.com/2010/POLITICS/08/13/democrats.social.security/index.html"
    val article = TestUtils.getArticle(url = url, rawHTML = html)
    val title = "Democrats to use Social Security against GOP this fall"
    val content = "Washington (CNN) -- Democrats pledged "
    TestUtils.runArticleAssertions(article = article, expectedTitle = title, expectedStart = content)
  }

  @Test
  def techcrunch1() {
    implicit val config = TestUtils.NO_IMAGE_CONFIG
    val html = FileHelper.loadResourceFile(resDir + "techcrunch1.txt", Goose.getClass)
    val url = "http://techcrunch.com/2011/08/13/2005-zuckerberg-didnt-want-to-take-over-the-world/"
    val content = "The Huffington Post has come across this fascinating five-minute interview"
    val title = "2005 Zuckerberg Didn’t Want To Take Over The World"
    val article = TestUtils.getArticle(url = url, rawHTML = html)
    TestUtils.runArticleAssertions(article = article, expectedTitle = title, expectedStart = content)
  }

  @Test
  def businessweek1() {
    implicit val config = TestUtils.NO_IMAGE_CONFIG
    val html = FileHelper.loadResourceFile(resDir + "businessweek1.txt", Goose.getClass)
    val url: String = "http://www.businessweek.com/magazine/content/10_34/b4192066630779.htm"
    val title = "Olivia Munn: Queen of the Uncool"
    val content = "Six years ago, Olivia Munn arrived in Hollywood with fading ambitions of making it as a sports reporter and set about deploying"
    val article = TestUtils.getArticle(url = url, rawHTML = html)
    TestUtils.runArticleAssertions(article = article, expectedTitle = title, expectedStart = content)
  }
}