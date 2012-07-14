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
  * This list now just soccer websites
  */
class GoldSitesTestIT {

  @Test
  def folha() {
    implicit val config = TestUtils.DEFAULT_CONFIG

    val url     = "http://www1.folha.uol.com.br/esporte/1070420-leao-critica-regulamento-do-paulista-e-poe-culpa-na-tv.shtml"
    val content = "Após retomar a liderança do Campeonato Paulista, com a vitória do São Paulo de virada por 4 a 2 sobre o Ituano"
    val image   = "http://f.i.uol.com.br/folha/esporte/images/12084302.jpeg"
    val title   = "Leão critica regulamento do Paulista e põe culpa na TV"
    val article = TestUtils.getArticle(url)

    TestUtils.runArticleAssertions(article = article,
      expectedStart = content,
      expectedTitle = title,
      expectedImage = image)
  }


  @Test
  def lancenet() {
    implicit val config = TestUtils.DEFAULT_CONFIG

    val url     = "http://www.lancenet.com.br/sao-paulo/Leao-Arena-Barueri-casa-Tricolor_0_675532605.html"
    val content = "No próximo sábado, o São Paulo jogará, como mandante, na Arena Barueri diante do Mogi Mirim"
    val image   = "http://www.lancenet.com.br/futebol-general/Paulo-Catanduvense-Campeonato-Paulista-Fernandinho_LANIMA20120329_0148_25.jpg"
    val title   = "Para Leão, Arena Barueri não é casa do Tricolor"
    val article = TestUtils.getArticle(url)

    TestUtils.runArticleAssertions(article = article,
      expectedStart = content,
      expectedTitle = title,
      expectedImage = image)
  }

  @Test
  def globoesporte() {
    implicit val config = TestUtils.DEFAULT_CONFIG

    val url     = "http://globoesporte.globo.com/futebol/times/sao-paulo/noticia/2012/04/filho-do-gramado-leao-administra-o-sao-paulo-na-base-da-conversa.html"
    val content = "Emerson Leão não foi ao campo na manhã desta terça-feira no centro de treinamento do São Paulo"
    val image   = "http://s2.glbimg.com/DKjyTG2ZACkmIUmt2NcSIuR8k48J3DLeS8Txhob9fJz2lXAYXrrJq_ZurQ44i4Jn/s.glbimg.com/es/ge/f/original/2012/03/25/leao_ae_marioangelo.jpg"
    val title   = "'Filho do gramado', Leão administra o São Paulo na base da conversa"
    val article = TestUtils.getArticle(url)

    TestUtils.runArticleAssertions(article = article,
      expectedStart = content,
      expectedTitle = title,
      expectedImage = image)
  }
}

