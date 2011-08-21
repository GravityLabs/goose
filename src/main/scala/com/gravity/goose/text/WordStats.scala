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

