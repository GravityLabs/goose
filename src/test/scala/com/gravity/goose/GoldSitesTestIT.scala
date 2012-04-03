package com.gravity.goose

import org.junit.Test
import org.junit.Assert._
import org.jsoup.nodes.Element
import com.gravity.goose.extractors.{VoicesContentExtractor, AdditionalDataExtractor}

/**
  * Created by Jim Plush
  * User: jim
  * Date: 8/16/11
  * This class hits live websites and is only run manually, not part of the tests lifecycle
  */

/**
  * Updated by Marco Singer
  * User: marcosinger
  * Date: 4/3/12
  * This list is now just of soccer websites
  */
class GoldSitesTestIT {

  @Test
  def espn_brazil() {
    implicit val config = TestUtils.DEFAULT_CONFIG

    val url = "http://espn.estadao.com.br/saopaulo/noticia/248648_UM+ANO+APOS+APRESENTACAO+PARA+45+MIL+LUIS+FABIANO+ACUMULA+TRES+LESOES+E+14+GOLS"
    val content = "Há exatamente um ano, cerca de 45 mil pessoas faziam festa no Morumbi"
    val image = "http://contenti1.espn.com.br/foto/pequena/1d5b0d8f-1d44-3ade-a582-5c9d8700773c.gif"
    val title = "Um ano após apresentação para 45 mil, Luis Fabiano acumula três lesões e 14 gols"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article, expectedTitle = title, expectedImage = image)
  }
}

