package com.gravity.goose

import org.junit.Test
import org.junit.Assert._
import com.gravity.goose.extractors.VoicesContentExtractor

/**
  * Created by Jim Plush
  * User: jim
  * Date: 8/16/11
  * This class hits live websites and is only run manually, not part of the tests lifecycle
  */
class AicaiTest {

  @Test
  def testArticleElementedArticle() { // to verify issue #56 is resolved
    implicit val config = TestUtils.NO_IMAGE_CONFIG
    //val url = "http://www.csdn.net/article/2014-04-14/2819287-what-supercell-did-next"
    val url = "http://sports.sina.com.cn/g/laliga/2014-04-14/11447118806.shtml"
    
    val goose = new Goose(config)
    
    val article = goose.extractContent(url)
    
    println(article.cleanedArticleText)
  }
}