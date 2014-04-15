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

import com.gravity.goose.utils.FileHelper
import com.gravity.goose.Language._
import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;
import java.io.StringReader;
import scala.collection.JavaConversions._
import java.util.HashMap
import scala.collection.Set
import java.util.Map

object StopWords {

  // the confusing pattern below is basically just match any non-word character excluding white-space.
  private val PUNCTUATION: StringReplacement = StringReplacement.compile("[^\\p{Ll}\\p{Lu}\\p{Lt}\\p{Lo}\\p{Nd}\\p{Pc}\\s]", string.empty)

  //val STOP_WORDS = FileHelper.loadResourceFile("stopwords-en.txt", StopWords.getClass).split(sys.props("line.separator")).toSet
  private var stopWordsMap: Map[String, Set[String]] = new HashMap[String, Set[String]]()

  def removePunctuation(str: String): String = {
    PUNCTUATION.replaceAll(str)
  }
  
  def getStopWords(language: Language): Set[String] = {
    val lname = language.toString()
    var stopWords = stopWordsMap.get(lname)
    if (stopWords == null) {
      var stopWordsFile = "stopwords-%s.txt" format lname
      stopWords = FileHelper.loadResourceFile(stopWordsFile, StopWords.getClass).split(sys.props("line.separator")).toSet
      stopWords = stopWords.map(s=>s.trim)
      stopWordsMap.put(lname, stopWords)
    }
    stopWords    
  }

  def getCandidateWords(strippedInput: String, language: Language): Array[String] = {
	language match {
	  case English => string.SPACE_SPLITTER.split(strippedInput)
	  case Chinese => tokenize(strippedInput).toArray
	  case _ => string.SPACE_SPLITTER.split(strippedInput)
	}
  } 
  
  def getStopWordCount(content: String, language: Language): WordStats = {

    if (string.isNullOrEmpty(content)) return WordStats.EMPTY
    val ws: WordStats = new WordStats
    val strippedInput: String = removePunctuation(content)

    val candidateWords = getCandidateWords(strippedInput, language)
      
    var overlappingStopWords: List[String] = List[String]()

    val STOP_WORDS = getStopWords(language)
    
    candidateWords.foreach(w => {
       if (STOP_WORDS.contains(w.toLowerCase)) {
         overlappingStopWords = w.toLowerCase :: overlappingStopWords
       }
    })
    ws.setWordCount(candidateWords.length)
    ws.setStopWordCount(overlappingStopWords.size)
    ws.setStopWords(overlappingStopWords)
    ws
  }
  
  def  tokenize(line: String): List[String] = {
    
    var seg = new ComplexSeg(Dictionary.getInstance());
    var mmSeg = new MMSeg(new StringReader(line), seg);
    var tokens = List[String]();
    var word = mmSeg.next()
    while (word != null) {
      tokens = word.getString() :: tokens ;
      word = mmSeg.next();
    }
    println(tokens)
    return tokens;
  }  
}