package com.gravity.goose

import org.junit.Test
import org.junit.Assert._
import scala.actors.Future
import scala.actors.Futures._

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/14/11
 */

class GooseTest {

  @Test
  def getLink {

    implicit val config = new Configuration
    val url = "http://techcrunch.com/2011/08/13/2005-zuckerberg-didnt-want-to-take-over-the-world/"
    val goose = new Goose
    val article = goose.extractContent(url)

    println("\nFINAL TALLY")
    println("\nTEXT: \n" + article.cleanedArticleText)
    println("\nIMAGE: "+ article.topImage.getImageSrc)

  }


}