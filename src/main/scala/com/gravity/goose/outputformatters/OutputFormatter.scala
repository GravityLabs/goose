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

package com.gravity.goose.outputformatters

import org.jsoup.nodes._
import org.apache.commons.lang.StringEscapeUtils
import org.jsoup.select.Elements
import com.gravity.goose.utils.Logging
import com.gravity.goose.text.{StopWords, WordStats}
import scala.collection.JavaConversions._
import collection.mutable.ListBuffer

/**
* Created by Jim Plush
* User: jim
* Date: 8/17/11
*/

trait OutputFormatter extends Logging {
  val logPrefix = "outformat: "

  private var topNode: Element = null
  /**
  * Depricated use {@link #getFormattedText(Element)}
  * @param topNode the top most node to format
  * @return the prepared Element
  */
  @Deprecated def getFormattedElement(topNode: Element): Element = {
    this.topNode = topNode
    removeNodesWithNegativeScores
    convertLinksToText
    replaceTagsWithText
    removeParagraphsWithFewWords()
    topNode
  }

  /**
  * Removes all unnecessarry elements and formats the selected text nodes
  * @param topNode the top most node to format
  * @return a formatted string with all HTML removed
  */
  def getFormattedText(topNode: Element): String = {
    this.topNode = topNode
    removeNodesWithNegativeScores
    convertLinksToText
    replaceTagsWithText
    removeParagraphsWithFewWords()
    convertToText
  }

  /**
  * Depricated use {@link #getFormattedText(Element)}
  * takes an element and turns the P tags into \n\n
  *
  * @return
  */
  def convertToText: String = {

    val textElements = (topNode.children().map((e: Element) => {
      StringEscapeUtils.unescapeHtml(e.text).trim
    })).toList.mkString("\n\n")

    textElements

  }

  /**
  * cleans up and converts any nodes that should be considered text into text
  */
  private def convertLinksToText: Unit = {
    trace(logPrefix + "Turning links to text")

    val links: Elements = topNode.getElementsByTag("a")
    for (item <- links) {
      if (item.getElementsByTag("img").size == 0) {
        val tn: TextNode = new TextNode(item.text, topNode.baseUri)
        item.replaceWith(tn)
      }
    }
  }

  /**
  * if there are elements inside our top node that have a negative gravity score, let's
  * give em the boot
  */
  private def removeNodesWithNegativeScores: Unit = {
    var gravityItems: Elements = this.topNode.select("*[gravityScore]")
    import scala.collection.JavaConversions._
    for (item <- gravityItems) {
      var score: Int = Integer.parseInt(item.attr("gravityScore"))
      if (score < 1) {
        item.remove
      }
    }
  }

  /**
  * replace common tags with just text so we don't have any crazy formatting issues
  * so replace <br>, <i>, <strong>, etc.... with whatever text is inside them
  */
  private def replaceTagsWithText: Unit = {


    val bolds: Elements = topNode.getElementsByTag("b")
    for (item <- bolds) {
      val tn: TextNode = new TextNode(getTagCleanedText(item), topNode.baseUri)
      item.replaceWith(tn)
    }

    val strongs: Elements = topNode.getElementsByTag("strong")
    for (item <- strongs) {
      val tn: TextNode = new TextNode(getTagCleanedText(item), topNode.baseUri)
      item.replaceWith(tn)
    }

    val italics: Elements = topNode.getElementsByTag("i")
    for (item <- italics) {
      val tn: TextNode = new TextNode(getTagCleanedText(item), topNode.baseUri)
      item.replaceWith(tn)

    }

  }

  private def getTagCleanedText(item: Node): String = {

    // used to remove tags within tags
    val tagReplace = "<[^>]+>".r
    val sb = new StringBuilder()
    for (child <- item.childNodes()) {
      if (child.isInstanceOf[TextNode]) {
        sb.append(child.asInstanceOf[TextNode].getWholeText)
      } else if (child.isInstanceOf[Element]) {
        sb.append(child.asInstanceOf[Element].outerHtml())
      }
    }
    val text = tagReplace replaceAllIn(sb.toString(), "")
    text
  }

  /**
  * remove paragraphs that have less than x number of words, would indicate that it's some sort of link
  */
  private def removeParagraphsWithFewWords() {
    if (logger.isDebugEnabled) {
      logger.debug("removeParagraphsWithFewWords starting...")
    }
    val allNodes: Elements = this.topNode.getAllElements
    import scala.collection.JavaConversions._
    for (el <- allNodes) {
      try {
        val stopWords: WordStats = StopWords.getStopWordCount(el.text)
        if (stopWords.getStopWordCount < 3 && el.getElementsByTag("object").size == 0 && el.getElementsByTag("embed").size == 0) {
          logger.debug("removeParagraphsWithFewWords - swcnt: %d removing text: %s".format(stopWords.getStopWordCount, el.text()))
          el.remove()
        }
      }
      catch {
        case e: IllegalArgumentException => {
          logger.error(e.getMessage)
        }
      }
    }
  }
}