package com.gravity.goose.text

import org.junit.Test
import org.junit.Assert._

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/14/11
 */

class HashUtilsTest {

  @Test
  def generateMD5() {
    val normalUrl = "http://gravity.com/article/url1"
    val expectedHash = "2d822fac222b087bd83d87c7b6546687"
    assertEquals(expectedHash, HashUtils.md5(normalUrl))
  }
}