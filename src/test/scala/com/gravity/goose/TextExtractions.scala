package com.gravity.goose

import org.junit.Test
import org.junit.Assert._
import utils.FileHelper

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/19/11
 */

class TextExtractions {


  @Test
  def cnn1() {
    implicit val config = TestUtils.NO_IMAGE_CONFIG
    val html = FileHelper.loadResourceFile(TestUtils.staticHtmlDir + "cnn1.txt", Goose.getClass)
    val url = "http://www.cnn.com/2010/POLITICS/08/13/democrats.social.security/index.html"
    val article = TestUtils.getArticle(url = url, rawHTML = html)
    val title = "Democrats to use Social Security against GOP this fall"
    val content = "Washington (CNN) -- Democrats pledged "
    TestUtils.runArticleAssertions(article = article, expectedTitle = title, expectedStart = content)
  }

  @Test
  def techcrunch1() {
    implicit val config = TestUtils.NO_IMAGE_CONFIG
    val html = FileHelper.loadResourceFile(TestUtils.staticHtmlDir + "techcrunch1.txt", Goose.getClass)
    val url = "http://techcrunch.com/2011/08/13/2005-zuckerberg-didnt-want-to-take-over-the-world/"
    val content = "The Huffington Post has come across this fascinating five-minute interview"
    val title = "2005 Zuckerberg Didn’t Want To Take Over The World"
    val article = TestUtils.getArticle(url = url, rawHTML = html)
    TestUtils.runArticleAssertions(article = article, expectedTitle = title, expectedStart = content)
  }

  @Test
  def businessweek1() {
    implicit val config = TestUtils.NO_IMAGE_CONFIG
    val html = FileHelper.loadResourceFile(TestUtils.staticHtmlDir + "businessweek1.txt", Goose.getClass)
    val url: String = "http://www.businessweek.com/magazine/content/10_34/b4192066630779.htm"
    val title = "Olivia Munn: Queen of the Uncool"
    val content = "Six years ago, Olivia Munn arrived in Hollywood with fading ambitions of making it as a sports reporter and set about deploying"
    val article = TestUtils.getArticle(url = url, rawHTML = html)
    TestUtils.runArticleAssertions(article = article, expectedTitle = title, expectedStart = content)
  }

  @Test
  def foxNews() {
    implicit val config = TestUtils.NO_IMAGE_CONFIG
    val html = FileHelper.loadResourceFile(TestUtils.staticHtmlDir + "foxnews1.txt", Goose.getClass)
    val url: String = "http://www.foxnews.com/politics/2010/08/14/russias-nuclear-help-iran-stirs-questions-improved-relations/"
    val content = "Russia's announcement that it will help Iran get nuclear fuel is raising questions"
    val article = TestUtils.getArticle(url = url, rawHTML = html)
    TestUtils.runArticleAssertions(article = article, expectedStart = content)

  }

  @Test
  def aolNews() {
    implicit val config = TestUtils.NO_IMAGE_CONFIG
    val html = FileHelper.loadResourceFile(TestUtils.staticHtmlDir + "aol1.txt", Goose.getClass)
    val url: String = "http://www.aolnews.com/nation/article/the-few-the-proud-the-marines-getting-a-makeover/19592478"
    val article = TestUtils.getArticle(url = url, rawHTML = html)
    val content = "WASHINGTON (Aug. 13) -- Declaring \"the maritime soul of the Marine Corps\" is"
    TestUtils.runArticleAssertions(article = article, expectedStart = content)
  }


  @Test
  def testHuffingtonPost() {
    implicit val config = TestUtils.NO_IMAGE_CONFIG
    val url: String = "http://www.huffingtonpost.com/2010/08/13/federal-reserve-pursuing_n_681540.html"
    val html = FileHelper.loadResourceFile(TestUtils.staticHtmlDir + "huffpo1.txt", Goose.getClass)

    val title: String = "Federal Reserve's Low Rate Policy Is A 'Dangerous Gamble,' Says Top Central Bank Official"
    val content = "A top regional Federal Reserve official sharply criticized Friday"
    val keywords = "federal, reserve's, low, rate, policy, is, a, 'dangerous, gamble,', says, top, central, bank, official, business"
    val description = "A top regional Federal Reserve official sharply criticized Friday the Fed's ongoing policy of keeping interest rates near zero -- and at record lows -- as a \"dangerous gamble.\""
    val article = TestUtils.getArticle(url = url, rawHTML = html)
    TestUtils.runArticleAssertions(article = article, expectedTitle = title, expectedStart = content, expectedDescription = description)

    val expectedTags = "Federal Open Market Committee" ::
        "Federal Reserve" ::
        "Federal Reserve Bank Of Kansas City" ::
        "Financial Crisis" ::
        "Financial Reform" ::
        "Financial Regulation" ::
        "Financial Regulatory Reform" ::
        "Fomc" ::
        "Great Recession" ::
        "Interest Rates" ::
        "Kansas City Fed" ::
        "Monetary Policy" ::
        "The Financial Fix" ::
        "Thomas Hoenig" ::
        "Too Big To Fail" ::
        "Wall Street Reform" ::
        "Business News" ::
        Nil
    assertNotNull("Tags should not be NULL!", article.tags)
    assertTrue("Tags should not be empty!", article.tags.size > 0)

    for (actualTag <- article.tags) {
      assertTrue("Each Tag should be contained in the expected set!", expectedTags.contains(actualTag))
    }
  }


  @Test
  def wallStreetJournal() {
    implicit val config = TestUtils.NO_IMAGE_CONFIG
    val html = FileHelper.loadResourceFile(TestUtils.staticHtmlDir + "wsj1.txt", Goose.getClass)
    val url: String = "http://online.wsj.com/article/SB10001424052748704532204575397061414483040.html"
    val article = TestUtils.getArticle(url = url, rawHTML = html)
    val content = "The Obama administration has paid out less than a third of the nearly $230 billion"
    TestUtils.runArticleAssertions(article = article, expectedStart = content)
  }

  @Test
  def usaToday() {
    implicit val config = TestUtils.NO_IMAGE_CONFIG
    val html = FileHelper.loadResourceFile(TestUtils.staticHtmlDir + "usatoday1.txt", Goose.getClass)
    val url: String = "http://content.usatoday.com/communities/thehuddle/post/2010/08/brett-favre-practices-set-to-speak-about-return-to-minnesota-vikings/1"
    val article = TestUtils.getArticle(url, rawHTML = html)
    val content = "Brett Favre says he couldn't give up on one more chance"
    TestUtils.runArticleAssertions(article = article, expectedStart = content)
  }
}