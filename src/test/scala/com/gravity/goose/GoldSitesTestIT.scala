package com.gravity.goose

import images.Image
import org.junit.Test
import junit.framework.Assert._

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/16/11
 * This class hits live websites and is only run manually, not part of the tests lifecycle
 */
class GoldSitesTestIT {

  import GoldSitesTestIT._

  private val NL = '\n';
  private val TAB = "\t\t";
  private val articleReport = new StringBuilder("=======================::. ARTICLE REPORT .::======================\n");

  @Test
  def techCrunch() {
    implicit val config = DEFAULT_CONFIG
    val url = "http://techcrunch.com/2011/08/13/2005-zuckerberg-didnt-want-to-take-over-the-world/"
    val content = "The Huffington Post has come across this fascinating five-minute interview"
    val image = "http://tctechcrunch2011.files.wordpress.com/2011/08/screen-shot-2011-08-13-at-4-55-35-pm.png?w=288"
    val title = "2005 Zuckerberg Didn’t Want To Take Over The World"
    val article = getArticle(url)
    runArticleAssertions(article = article, expectedTitle = title, expectedImage = image, expectedStart = content)
  }

  @Test
  def cnn() {
    implicit val config = DEFAULT_CONFIG
    val url = "http://www.cnn.com/2010/POLITICS/08/13/democrats.social.security/index.html"
    val article = getArticle(url)

    val title = "Democrats to use Social Security against GOP this fall"
    val content = "Washington (CNN) -- Democrats pledged "
    val image = "http://i.cdn.turner.com/cnn/2010/POLITICS/08/13/democrats.social.security/story.kaine.gi.jpg"
    runArticleAssertions(article = article, expectedTitle = title, expectedStart = content, expectedImage = image)
    printReport()
  }

  @Test
  def desertNews() {
    implicit val config = DEFAULT_CONFIG
    val url = "http://www.deseretnews.com/article/705388385/High-school-basketball-Top-Utah-prospects-representing-well.html"
    val article = getArticle(url)
    val content = "In going up against some of the top AAU basketball teams in"
    runArticleAssertions(article, expectedStart = content)

  }

  /**
  * returns an article object from a crawl
  */
  def getArticle(url: String)(implicit config: Configuration): Article = {
    val goose = new Goose
    val article = goose.extractContent(url)
    article
  }

  private def runArticleAssertions(article: Article, expectedTitle: String = null, expectedStart: String = null, expectedImage: String = null, expectedDescription: String = null, expectedKeywords: String = null): Unit = {
    articleReport.append("URL:      ").append(TAB).append(article.finalUrl).append(NL)
    articleReport.append("TITLE:    ").append(TAB).append(article.title).append(NL)
    articleReport.append("IMAGE:    ").append(TAB).append(article.topImage.getImageSrc).append(NL)
    articleReport.append("IMGKIND:  ").append(TAB).append(article.topImage.imageExtractionType).append(NL)
    articleReport.append("CONTENT:  ").append(TAB).append(article.cleanedArticleText.replace("\n", "    ")).append(NL)
    articleReport.append("METAKW:   ").append(TAB).append(article.metaKeywords).append(NL)
    articleReport.append("METADESC: ").append(TAB).append(article.metaDescription).append(NL)
    articleReport.append("DOMAIN:   ").append(TAB).append(article.domain).append(NL)
    articleReport.append("LINKHASH: ").append(TAB).append(article.linkhash).append(NL)
    articleReport.append("MOVIES:   ").append(TAB).append(article.movies).append(NL)
    articleReport.append("TAGS:     ").append(TAB).append(article.tags).append(NL)

    assertNotNull("Resulting article was NULL!", article)

    if (expectedTitle != null) {
      val title: String = article.title
      assertNotNull("Title was NULL!", title)
      assertEquals("Expected title was not returned!", expectedTitle, title)
    }
    if (expectedStart != null) {
      val articleText: String = article.cleanedArticleText
      assertNotNull("Resulting article text was NULL!", articleText)
      assertTrue("Article text was not as long as expected beginning!", expectedStart.length <= articleText.length)
      val actual: String = articleText.substring(0, expectedStart.length)
      assertEquals("The beginning of the article text was not as expected!", expectedStart, actual)
    }
    if (expectedImage != null) {
      val image: Image = article.topImage
      assertNotNull("Top image was NULL!", image)
      val src: String = image.getImageSrc
      assertNotNull("Image src was NULL!", src)
      assertEquals("Image src was not as expected!", expectedImage, src)
    }
    if (expectedDescription != null) {
      val description: String = article.metaDescription
      assertNotNull("Meta Description was NULL!", description)
      assertEquals("Meta Description was not as expected!", expectedDescription, description)
    }
    if (expectedKeywords != null) {
      val keywords: String = article.metaDescription
      assertNotNull("Meta Keywords was NULL!", keywords)
      assertEquals("Meta Keywords was not as expected!", expectedKeywords, keywords)
    }
  }

  protected def printReport() {
    println(articleReport)
  }
}


object GoldSitesTestIT {
  val DEFAULT_CONFIG: Configuration = new Configuration
  val NO_IMAGE_CONFIG: Configuration = new Configuration
  NO_IMAGE_CONFIG.enableImageFetching = false
}