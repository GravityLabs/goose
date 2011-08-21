package com.gravity.goose

import org.junit.Test
import org.junit.Assert._
/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/14/11
 */

class ArticleTest {

  @Test
  def newArticle() {
    val a = new Article()

    assertTrue(a.isInstanceOf[Article])
  }
}