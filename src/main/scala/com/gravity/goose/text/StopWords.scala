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

import java.util.{ArrayList, List}
import com.gravity.goose.utils.{Logging, FileHelper}

import scala.collection.immutable.Map

object StopWords extends Logging{

  import  me.champeau.ld.UberLanguageDetector
  val detector = UberLanguageDetector.getInstance()

  // the confusing pattern below is basically just match any non-word character excluding white-space.
  private val PUNCTUATION: StringReplacement = StringReplacement.compile("[^\\p{Ll}\\p{Lu}\\p{Lt}\\p{Lo}\\p{Nd}\\p{Pc}\\s]", string.empty)
  
  val STOP_WORDS = Map("ca" -> FileHelper.loadResourceFile("ca.txt", StopWords.getClass).split(sys.props("line.separator")).toSet,
		  				"de" -> FileHelper.loadResourceFile("de.txt", StopWords.getClass).split(sys.props("line.separator")).toSet,
		  				"en" -> FileHelper.loadResourceFile("en.txt", StopWords.getClass).split(sys.props("line.separator")).toSet,
		  				"es" -> FileHelper.loadResourceFile("es.txt", StopWords.getClass).split(sys.props("line.separator")).toSet,
		  				"fr" -> FileHelper.loadResourceFile("fr.txt", StopWords.getClass).split(sys.props("line.separator")).toSet,
		  				"it" -> FileHelper.loadResourceFile("it.txt", StopWords.getClass).split(sys.props("line.separator")).toSet,
		  				"pt" -> FileHelper.loadResourceFile("pt.txt", StopWords.getClass).split(sys.props("line.separator")).toSet,
		  				"ko" -> FileHelper.loadResourceFile("ko.txt", StopWords.getClass).split(sys.props("line.separator")).toSet,
		  				"all" -> FileHelper.loadResourceFile("all.txt", StopWords.getClass).split(sys.props("line.separator")).toSet);

  def removePunctuation(str: String): String = {
    PUNCTUATION.replaceAll(str)
  }

  def getStopWordCount(content: String): WordStats = {
    if (string.isNullOrEmpty(content)) return WordStats.EMPTY
    val ws: WordStats = new WordStats
    val strippedInput: String = removePunctuation(content)

    val candidateWords: Array[String] = string.SPACE_SPLITTER.split(strippedInput)

    val overlappingStopWords: List[String] = new ArrayList[String]
    
    val languageCode : String = detectLanguage(content)
    val stopWords:Set[String] = STOP_WORDS(languageCode)

    val stopWordsMatcher = languageCode match {
      case "ko" => overlappingStopWordsMatherForKorean(_, _)
      case other => overlappingStopWordsMatcherForNormal(_, _)
    }
    candidateWords.foreach(w => {
      if(stopWordsMatcher(stopWords, w)) overlappingStopWords.add(w.toLowerCase)
    })
    ws.setWordCount(candidateWords.length)
    ws.setStopWordCount(overlappingStopWords.size)
    ws.setStopWords(overlappingStopWords)
    ws
  }

  def overlappingStopWordsMatherForKorean(stopWords:Set[String], w:String):Boolean = stopWords.exists(w.endsWith(_))
  def overlappingStopWordsMatcherForNormal(stopWords:Set[String],w:String):Boolean = stopWords.contains(w.toLowerCase)
  
  /**
   * This method returns the code of the language identified in the content
   * passed as parameter.
   */
  def detectLanguage(content: String): String = {
    val language = detector.detectLang(content)
    STOP_WORDS.keys.find(language == _).getOrElse("en")
  }

}
