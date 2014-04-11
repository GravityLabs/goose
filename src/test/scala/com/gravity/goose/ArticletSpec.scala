/**
 * Copyright (c) 2013 by Stone Gao
 */
package com.gravity.goose

import org.scalatest.FunSpec
import org.scalatest.matchers.MustMatchers._
import java.io.File

class ArticleSpec extends FunSpec {

  describe("send test") {
    it("should be true") {
      val a = new Article()

      true must be(a.isInstanceOf[Article])
    }
  }
}
