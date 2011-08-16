package com.gravity.goose.text

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/16/11
 */

import java.util._
import com.gravity.goose.utils.FileHelper

object StopWords {

  // the confusing pattern below is basically just match any non-word character excluding white-space.
  private val PUNCTUATION: StringReplacement = StringReplacement.compile("[^\\p{Ll}\\p{Lu}\\p{Lt}\\p{Lo}\\p{Nd}\\p{Pc}\\s]", string.empty)

  val STOP_WORDS = FileHelper.loadResourceFile("stopwords-en.txt", StopWords.getClass).split("\n").toSet


  def removePunctuation(str: String): String = {
    PUNCTUATION.replaceAll(str)
  }

  def getStopWordCount(content: String): WordStats = {

    if (string.isNullOrEmpty(content)) return WordStats.EMPTY
    val ws: WordStats = new WordStats
    val strippedInput: String = removePunctuation(content)

    val candidateWords: Array[String] = string.SPACE_SPLITTER.split(strippedInput)

    val overlappingStopWords: List[String] = new ArrayList[String]
    var i: Int = 0
    while (i < candidateWords.length) {
      {
        val word: String = candidateWords(i)
        val wordLower: String = word.toLowerCase
        if (STOP_WORDS.contains(wordLower)) overlappingStopWords.add(wordLower)
      }
      ({
        i += 1;
        i
      })
    }

    ws.setWordCount(candidateWords.length)
    ws.setStopWordCount(overlappingStopWords.size)
    ws.setStopWords(overlappingStopWords)
    ws
  }


}
