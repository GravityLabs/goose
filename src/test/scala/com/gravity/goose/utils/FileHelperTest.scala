package com.gravity.goose.utils

import org.junit.Test
import org.junit.Assert._
import com.gravity.goose.text.StopWords

/**
* Created by Jim Plush
* User: jim
* Date: 8/16/11
*/

class FileHelperTest {

  @Test
  def loadFileContents() {
    println("loading test")
    val txt = FileHelper.loadResourceFile("stopwords-en.txt", StopWords.getClass)
    assertTrue(txt.startsWith("a's"))
  }

}