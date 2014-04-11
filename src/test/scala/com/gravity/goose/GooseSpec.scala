package com.gravity.goose

import org.scalatest.FunSpec
import org.scalatest.matchers.MustMatchers._
import java.io.File


/**
 * Created with IntelliJ IDEA.
 * User: maojianxin
 * Date: 4/11/14
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
class GooseSpec extends FunSpec {

  describe("send test") {
    it("should be true") {
      implicit val config = new Configuration
      //val url = "http://sports.sina.com.cn/l/2014-04-11/10467114164.shtml"
      val url = "http://en.wiktionary.org/wiki/goose"
      val goose = new Goose(config)
      val article = goose.extractContent(url)
      println(article.title)

      Thread.sleep(2000)
      true must be(true)
    }
  }
}
