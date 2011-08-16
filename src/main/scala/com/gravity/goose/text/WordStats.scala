package com.gravity.goose.text

import java.util.ArrayList
import java.util.List

/**
* User: Jim Plush
* Date: Oct 29, 2010
* Time: 3:59:44 PM
*/
object WordStats {
  var EMPTY: WordStats = new WordStats
}

class WordStats {


  import WordStats._

  /**
  * total number of stopwords or good words that we can calculate
  */
  var stopWordCount: Int = 0
  /**
  * total number of words on a node
  */
  var wordCount: Int = 0
  /**
  * holds an actual list of the stop words we found
  */
  var stopWords: List[String] = new ArrayList[String]

  def getStopWords: List[String] = {
    stopWords
  }

  def setStopWords(words: List[String]) {
    stopWords = words
  }

  def getStopWordCount: Int = {
    stopWordCount
  }

  def setStopWordCount(wordcount: Int) {
    stopWordCount = wordcount
  }

  def getWordCount: Int = {
    wordCount
  }

  def setWordCount(cnt: Int) {
    wordCount = cnt
  }


}

