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

import com.gravity.goose.utils.{Logging, FileHelper}

import com.gravity.goose.Language._
import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;
import java.io.StringReader;
import scala.collection.JavaConversions._
import scala.collection.Set

object StopWords extends Logging{

  import  me.champeau.ld.UberLanguageDetector
  val detector = UberLanguageDetector.getInstance()

  // the confusing pattern below is basically just match any non-word character excluding white-space.
  private val PUNCTUATION: StringReplacement = StringReplacement.compile("[^\\p{Ll}\\p{Lu}\\p{Lt}\\p{Lo}\\p{Nd}\\p{Pc}\\s]", string.empty)
  
  val stopWordsMap = Map("ca" -> FileHelper.loadResourceFile("ca.txt", StopWords.getClass).split(sys.props("line.separator")).toSet,
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
    
    val languageCode : String = detectLanguage(content)
    val stopWords:Set[String] = stopWordsMap(languageCode)

    val stopWordsMatcher = languageCode match {
      case "ko" => overlappingStopWordsMatherForKorean(_, _)
      case other => overlappingStopWordsMatcherForNormal(_, _)
    }
    candidateWords.foreach(w => {
//TODO w.toLowercase ???
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
    stopWordsMap.keys.find(language == _).getOrElse("en")
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