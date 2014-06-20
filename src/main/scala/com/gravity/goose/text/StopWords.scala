/**
 * Licensed to Gravity.com under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Gravity.com licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gravity.goose.text

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/16/11
 */

import java.util.ArrayList
import java.util.List
import com.gravity.goose.utils.FileHelper

object StopWords {

  // the confusing pattern below is basically just match any non-word character excluding white-space.
  private val PUNCTUATION: StringReplacement = StringReplacement.compile("[^\\p{Ll}\\p{Lu}\\p{Lt}\\p{Lo}\\p{Nd}\\p{Pc}\\s]", string.empty)

  // TODO: there must a better way to do this. See
  // http://www.uofr.net/~greg/java/get-resource-listing.html?
  val LANGUAGES: Set[String] = Set("ar", "da", "de", "en", "es", "fi", "fr",
                                   "hu", "id", "it", "ko", "nb", "nl", "no",
                                   "pl", "pt", "ru", "sv", "zh")

  val STOP_WORDS: Map[String, Set[String]] =
    (LANGUAGES.view map {lang =>
      lang ->
      FileHelper.loadResourceFile("stopwords-" + lang + ".txt",
        StopWords.getClass).split(sys.props("line.separator")).toSet
    }).toMap.withDefaultValue(Set())

  def removePunctuation(str: String): String = {
    PUNCTUATION.replaceAll(str)
  }

  def getStopWordCount(content: String, lang: String = "en"): WordStats = {

    if (string.isNullOrEmpty(content)) return WordStats.EMPTY
    val ws: WordStats = new WordStats
    val strippedInput: String = removePunctuation(content)

    val candidateWords: Array[String] = string.SPACE_SPLITTER.split(strippedInput)

    val overlappingStopWords: List[String] = new ArrayList[String]

    val stopWords = STOP_WORDS(lang)
    if (stopWords.size > 0) {
      candidateWords.foreach(w => {
        if (stopWords.contains(w.toLowerCase)) overlappingStopWords.add(w.toLowerCase)
      })
    }
    ws.setWordCount(candidateWords.length)
    ws.setStopWordCount(overlappingStopWords.size)
    ws.setStopWords(overlappingStopWords)
    ws
  }

}
