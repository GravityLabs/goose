package com.gravity.goose

import images.Image
import junit.framework.Assert._
import com.gravity.goose.extractors.AdditionalDataExtractor
import org.jsoup.nodes.Element
import scala.util.Try

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/19/11
 */

object TestUtils {

  val staticHtmlDir = "/com/gravity/goose/statichtml/"
  private val NL = '\n';
  private val TAB = "\t\t";
  val articleReport = new StringBuilder("=======================::. ARTICLE REPORT .::======================\n");

  val DEFAULT_CONFIG: Configuration = new Configuration(
      localStoragePath=Try{java.io.File.createTempFile("temp", null).getParentFile().getAbsolutePath()}.getOrElse(null)
  )
  //DEFAULT_CONFIG. 
  val NO_IMAGE_CONFIG: Configuration = new Configuration
  NO_IMAGE_CONFIG.enableImageFetching = false

  object additionalExt extends AdditionalDataExtractor {
    override def extract(rootElement: Element) = {
      println()
      println("ADDITIONAL DATA EXTRACTOR CALLED")
      println()
      Map("test" -> "success")
    }
  }

  val ADDITIONAL_DATA_CONFIG = new Configuration
  ADDITIONAL_DATA_CONFIG.setAdditionalDataExtractor(additionalExt)

  /**
  * returns an article object from a crawl
  */
  def getArticle(url: String, rawHTML: String = null)(implicit config: Configuration): Article = {
    val goose = new Goose(config)
    val article = goose.extractContent(url, rawHTML)
//    goose.shutdownNetwork()
    article
  }

  def runArticleAssertions(article: Article, expectedTitle: String = null, expectedStart: String = null, expectedHtmlStart: String = null, expectedImage: String = null, expectedImages: List[String] = null, expectedDescription: String = null, expectedKeywords: String = null): Unit = {
    articleReport.append("URL:            ").append(TAB).append(article.finalUrl).append(NL)
    articleReport.append("TITLE:          ").append(TAB).append(article.title).append(NL)
    articleReport.append("IMAGE:          ").append(TAB).append(article.topImage.getImageSrc).append(NL)
    articleReport.append("All_IMGS:       ").append(TAB).append(article.allImages).append(NL)
    articleReport.append("IMGKIND:        ").append(TAB).append(article.topImage.imageExtractionType).append(NL)
    articleReport.append("ALL_IMAGES:    ").append(TAB).append(article.allImages.map((i: Image) => i.getImageSrc)).append(NL)
    articleReport.append("CONTENT:        ").append(TAB).append(article.cleanedArticleText.replace("\n", "    ")).append(NL)
    articleReport.append("HTML CONTENT:   ").append(TAB).append(article.htmlArticle).append(NL)
    articleReport.append("METAKW:         ").append(TAB).append(article.metaKeywords).append(NL)
    articleReport.append("METADESC:       ").append(TAB).append(article.metaDescription).append(NL)
    articleReport.append("DOMAIN:         ").append(TAB).append(article.domain).append(NL)
    articleReport.append("LINKHASH:       ").append(TAB).append(article.linkhash).append(NL)
    articleReport.append("MOVIES:         ").append(TAB).append(article.movies).append(NL)
    articleReport.append("TAGS:           ").append(TAB).append(article.tags).append(NL)

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
    if (expectedHtmlStart != null) {
      val articleHtml: String = article.htmlArticle
      assertNotNull("Resulting article html was NULL!", articleHtml)
      assertTrue("Article html was not as long as expected beginning!", expectedHtmlStart.length <= articleHtml.length)
      val actual: String = articleHtml.substring(0, expectedHtmlStart.length)
      assertEquals("The beginning of the article html was not as expected!", expectedHtmlStart, actual)
    }
    if (expectedImage != null) {
      val image: Image = article.topImage
      assertNotNull("Top image was NULL!", image)
      val src: String = image.getImageSrc
      assertNotNull("Image src was NULL!", src)
      assertEquals("Image src was not as expected!", expectedImage, src)
    }
    if (expectedImages != null) {
      val images: List[Image] = article.allImages
      assertNotNull("Images was NULL!", images)
      assertEquals("Different number of images then expected!", expectedImages.size, images.size)
      images.zip(expectedImages).foreach{ case (i: Image, ei: String) =>
        val src: String = i.getImageSrc
        assertNotNull("Image src was NULL!", src)
        assertEquals("Image src was not as expected!", ei, src)
      }
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

  def printReport() {
    println(articleReport)
  }
}
