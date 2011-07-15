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
package com.jimplush.goose.texthelpers;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Jim Plush
 * Date: Oct 29, 2010
 * Time: 3:59:44 PM
 */
public class WordStats {

  public static final WordStats EMPTY = new WordStats();

  /**
   * total number of stopwords or good words that we can calculate
   */
  private int stopWordCount = 0;

  /**
   * total number of words on a node
   */
  private int wordCount = 0;

  /**
   * holds an actual list of the stop words we found
   */
  private List<String> stopWords = new ArrayList<String>();

  public List<String> getStopWords() {
    return stopWords;
  }

  public void setStopWords(List<String> stopWords) {
    this.stopWords = stopWords;
  }


  public int getStopWordCount() {
    return stopWordCount;
  }

  public void setStopWordCount(int stopWordCount) {
    this.stopWordCount = stopWordCount;
  }

  public int getWordCount() {
    return wordCount;
  }

  public void setWordCount(int wordCount) {
    this.wordCount = wordCount;
  }


}
