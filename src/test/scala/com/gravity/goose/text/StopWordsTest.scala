package com.gravity.goose.text

import org.junit.Test
import org.junit.Assert._
import  scala.collection.JavaConversions._

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/14/11
 */

class StopWordsTest {

  @Test
  def findsHowManyStopWordsWeHave() {
    assertEquals(0, StopWords.getStopWordCount("blah blah blah").getStopWordCount)
    assertEquals(1, StopWords.getStopWordCount("although blah de blah").getStopWordCount)
  }

  @Test
  def determinesWhichWordsAreStopWords() {
    assertEquals(seqAsJavaList(List("although")), StopWords.getStopWordCount("although blah de blah").getStopWords)
    assertEquals(seqAsJavaList(List()), StopWords.getStopWordCount("blah de blah").getStopWords)
  }
}