package com.gravity.goose

import org.junit.Test
import org.junit.Assert._
import scala.io.Source
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.LogManager;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;

import com.gravity.goose.extractors.VoicesContentExtractor
import com.gravity.goose.text.StopWords


/**
  * Created by Jim Plush
  * User: jim
  * Date: 8/16/11
  * This class hits live websites and is only run manually, not part of the tests lifecycle
  */
class AicaiTest {

  @Test
  def testArticleElementedArticle() { // to verify issue #56 is resolved
    var config = TestUtils.NO_IMAGE_CONFIG
    config.language = Language.Chinese
    //val url = "http://www.csdn.net/article/2014-04-14/2819287-what-supercell-did-next"
    //val url = "http://sports.sina.com.cn/g/laliga/2014-04-14/11447118806.shtml"
    val url = "http://luoxiaowei.baijia.baidu.com/article/11833"
      
    val html = Source.fromURL(url)
    val goose = new Goose(config)
    val article = goose.extractContent(url)
    
    println(article.cleanedArticleText)
  }
  
  def  tokenize(line: String): List[String] = {
    var seg = new ComplexSeg(Dictionary.getInstance());
    var mmSeg = new MMSeg(new StringReader(line), seg);
    var tokens = List[String]();
    var word = mmSeg.next()
    while (word != null) {
      tokens = word.getString() :: tokens ;
      word = mmSeg.next();
    }
    return tokens;
  }    
}