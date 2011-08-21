package com.gravity.goose.utils

import org.junit.Test
import org.junit.Assert._

/**
* Created by Jim Plush
* User: jim
* Date: 8/14/11
*/

class URLHelperTest {

  @Test
  def finalUrlGetCleaned() {
    val normalUrl = "http://techcrunch.com/test/url1"
    assertEquals(normalUrl, URLHelper.getCleanedUrl(normalUrl).get.url.toString)

    val escapedFragment = "http://lifehacker.com/#!5659837/build-a-rocket-stove-to-heat-your-home-with-wood-scraps"
    val expectedUrl = "http://lifehacker.com/?_escaped_fragment_=5659837/build-a-rocket-stove-to-heat-your-home-with-wood-scraps"
    assertEquals(expectedUrl, URLHelper.getCleanedUrl(escapedFragment).get.url.toString)
    assertEquals(expectedUrl, URLHelper.getCleanedUrl(escapedFragment).get.urlString)
  }


}