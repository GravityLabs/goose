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
  def badlink() {
    implicit val config = new Configuration
    val url = "http://nolove888.com/2011/08/13/LINKNOTEXISTS"
    val goose = new Goose(config)
    val article = goose.extractContent(url)
    assertNull(article.topNode)
  }


}
