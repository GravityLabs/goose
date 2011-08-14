package com.gravity.goose

import org.junit.Test
import org.junit.Assert._

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/14/11
 */

class GooseTest {

  @Test
  def getLink {

    val url = "http://techcrunch.com/2011/08/13/2005-zuckerberg-didnt-want-to-take-over-the-world/"
    val goose = new Goose
    goose.extractContent(url)

  }
}