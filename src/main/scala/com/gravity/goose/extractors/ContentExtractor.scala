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
package com.gravity.goose.extractors

import com.gravity.goose.Article
import com.gravity.goose.text._
import com.gravity.goose.utils.Logging
import java.net.URL
import java.util.ArrayList
import java.util.Date
import scala.collection.mutable
import scala.collection.JavaConversions._
import org.jsoup.nodes.{Attributes, Element, Document}
import org.jsoup.select._
import com.gravity.goose.Language._
import scala.math._
/**
* Created by Jim Plush
* User: jim
* Date: 8/15/11
*/
object ContentExtractor extends Logging {
  val logPrefix = "ContentExtractor: "
}

trait ContentExtractor {
  import ContentExtractor._

  def getLogger() = logger

  // PRIVATE PROPERTIES BELOW

  val MOTLEY_REPLACEMENT: StringReplacement = StringReplacement.compile("&#65533;", string.empty)
  val ESCAPED_FRAGMENT_REPLACEMENT: StringReplacement = StringReplacement.compile("#!", "?_escaped_fragment_=")
  val TITLE_REPLACEMENTS: ReplaceSequence = ReplaceSequence.create("&raquo;").append("»")
  val PIPE_SPLITTER: StringSplitter = new StringSplitter("\\|")
  val DASH_SPLITTER: StringSplitter = new StringSplitter(" - ")
  val ARROWS_SPLITTER: StringSplitter = new StringSplitter("»")
  val COLON_SPLITTER: StringSplitter = new StringSplitter(":")
  val SPACE_SPLITTER: StringSplitter = new StringSplitter(" ")
  val NO_STRINGS = Set.empty[String]
  val A_REL_TAG_SELECTOR: String = "a[rel=tag], a[href*=/tag/]"
  val TOP_NODE_TAGS = new TagsEvaluator(Set("p", "td", "pre", "li", "code"))

  def getTitle(article: Article): String = {
    var title: String = string.empty

    val doc = article.doc

    try {
      val titleElem: Elements = doc.getElementsByTag("title")
      if (titleElem == null || titleElem.isEmpty) return string.empty
      var titleText: String = titleElem.first.text
      if (string.isNullOrEmpty(titleText)) return string.empty
      var usedDelimeter: Boolean = false
      if (titleText.contains("|")) {
        titleText = doTitleSplits(titleText, PIPE_SPLITTER)
        usedDelimeter = true
      }
      if (!usedDelimeter && titleText.contains("-")) {
        titleText = doTitleSplits(titleText, DASH_SPLITTER)
        usedDelimeter = true
      }
      if (!usedDelimeter && titleText.contains("»")) {
        titleText = doTitleSplits(titleText, ARROWS_SPLITTER)
        usedDelimeter = true
      }
      if (!usedDelimeter && titleText.contains(":")) {
        titleText = doTitleSplits(titleText, COLON_SPLITTER)
      }
      // todo do we still need this? thinking not, make it an output formatter config
      //      title = StringEscapeUtils.escapeHtml(titleText)
      title = MOTLEY_REPLACEMENT.replaceAll(titleText)

      trace(logPrefix + "Page title is: " + title)

      title

    }
    catch {
      case e: NullPointerException => {
        warn(e.toString)
        string.empty
      }
    }

  }

  /**
  * based on a delimeter in the title take the longest piece or do some custom logic based on the site
  *
  * @param title
  * @param splitter
  * @return
  */
  def doTitleSplits(title: String, splitter: StringSplitter): String = {
    var largestTextLen: Int = 0
    var largestTextIndex: Int = 0
    val titlePieces: Array[String] = splitter.split(title)
    if (titlePieces.length > 0) {
      var i: Int = 0
      while (i < titlePieces.length) {
        val current: String = titlePieces(i)
        if (current.length > largestTextLen) {
          largestTextLen = current.length
          largestTextIndex = i
        }
        i += 1
      }

      TITLE_REPLACEMENTS.replaceAll(titlePieces(largestTextIndex)).trim
    }
    else null
  }

  private def getMetaContent(doc: Document, metaName: String): String = {
    val meta: Elements = doc.select(metaName)
    var content: String = null
    if (meta.size > 0) {
      content = meta.first.attr("content")
    }
    if (string.isNullOrEmpty(content)) string.empty else content.trim
  }

  /**
  * if the article has meta description set in the source, use that
  */
  def getMetaDescription(article: Article): String = {
    var desc = article.doc.select("meta[name=description]").attr("content")
    if (desc.isEmpty) {
      desc = article.doc.select("meta[property=og:description]").attr("content")
      if (desc.isEmpty) {
        desc = article.doc.select("meta[name=twitter:description]").attr("content")
      }
    }

    if (desc.nonEmpty) {
      desc.trim
    } else {
      string.empty
    }
  }

  /**
  * if the article has meta keywords set in the source, use that
  */
  def getMetaKeywords(article: Article): String = {
    getMetaContent(article.doc, "meta[name=keywords]")
  }


  /**
   * if the article has meta canonical link set in the url
   */
  def getCanonicalLink(article: Article): String = {
    var url = article.doc.select("link[rel=canonical]").attr("abs:href")
    trace(logPrefix + " base uri: " + article.doc.baseUri)
    trace(logPrefix + " canonical link: " + url)

    if (url.isEmpty) {
      url = article.doc.select("meta[property=og:url]").attr("abs:content")

      trace(logPrefix + " canonical link meta og: " + url)
      if (url.isEmpty) {
        url = article.doc.select("meta[name=twitter:url]").attr("abs:content")

        trace(logPrefix + " canonical link meta twitter: " + url)
      }
    }
    if (url.nonEmpty) {
      val href = url.trim
      if (href.nonEmpty) href else article.finalUrl
    } else {
      article.finalUrl
    }
  }

  def getDomain(url: String): String = {
    new URL(url).getHost
  }

  def extractTags(article: Article): Set[String] = {
    val node = article.doc
    if (node.children.size == 0) return NO_STRINGS
    val elements: Elements = Selector.select(A_REL_TAG_SELECTOR, node)
    if (elements.size == 0) return NO_STRINGS
    val tags = mutable.HashSet[String]()

    for (el <- elements) {
      var tag: String = el.text
      if (!string.isNullOrEmpty(tag)) tags += tag
    }
    tags.toSet
  }

  def getDateFromURL(url: String): Date = {
    val path = new URL(url).getPath

    var year: Integer = -1;
    var yearCounter: Integer = -1;
    var month: Integer = -1;
    var monthCounter: Integer = -1;
    var day: Integer = -1;
    var done: Boolean = false
    val strs = path.split("/");
    for ((str, counter) <- strs.zipWithIndex) {
      if (!done) {
        if (str.length() == 4 && yearCounter < 0) {
          try {
            year = Integer.parseInt(str);
            if (year < 1970 || year > 3000) {
              year = -1;
            } else {
              trace(logPrefix + " found year: " + year)
              yearCounter = counter;
            }
          } catch {
            case _ : java.lang.NumberFormatException => None
          }
        } else if (str.length() == 2) {
          if (monthCounter < 0 && counter == yearCounter + 1) {
            try {
              month = Integer.parseInt(str);
              if (month < 1 || month > 12) {
                month = -1;
              } else {
                trace(logPrefix + " found month: " + month)
                monthCounter = counter;
              }
            } catch {
              case _ : java.lang.NumberFormatException => None
            }
          } else if (counter == monthCounter + 1) {
            try {
              day = Integer.parseInt(str);
              if (day < 1 || day > 31) {
                day = -1;
              } else {
                trace(logPrefix + " found day: " + day)
                done = true
              }
            } catch {
              case _ : java.lang.NumberFormatException => None
            }
          }
        }
      }
    }

    // should be converted to use jodatime or something, because java's date is terrible
    if (year < 0) return null;
    year = year - 1900 // date constructor takes year - 1900
    if (month < 1) return new Date(year, 0, 1)
    month = month - 1 // date constructor dates month in 0 - 11
    if (day < 1) return new Date(year, month, 1)
    return new Date(year, month, day)
  }

  /**
  * we're going to start looking for where the clusters of paragraphs are. We'll score a cluster based on the number of stopwords
  * and the number of consecutive paragraphs together, which should form the cluster of text that this node is around
  * also store on how high up the paragraphs are, comments are usually at the bottom and should get a lower score
  *
  * // todo refactor this long method
  * @return
  */

  def calculateBestNodeBasedOnClustering(article: Article, language: Language): Option[Element] = {
    trace(logPrefix + "Starting to calculate TopNode")
    val doc = article.doc.clone
    var topNode: Element = null
    val nodesToCheck = Collector.collect(TOP_NODE_TAGS, doc)
    var startingBoost: Double = 1.0
    var cnt: Int = 0
    var i: Int = 0
    val parentNodes = mutable.HashSet[Element]()
    val nodesWithText = mutable.Buffer[Element]()
    for (node <- nodesToCheck) {
      val nodeText: String = node.text
      val wordStats: WordStats = StopWords.getStopWordCount(nodeText, language)
      val highLinkDensity: Boolean = isHighLinkDensity(node)
      if (wordStats.getStopWordCount > 2 && !highLinkDensity) {
        nodesWithText.add(node)
      }
    }
    val numberOfNodes: Int = nodesWithText.size
    val negativeScoring: Int = 0
    val bottomNodesForNegativeScore: Double = numberOfNodes * 0.25

    trace(logPrefix + "About to inspect num of nodes with text: " + numberOfNodes)

    for (node <- nodesWithText) {
      var boostScore: Float = 0
      if (isOkToBoost(node, language)) {
        if (cnt >= 0) {
          boostScore = ((1.0 / startingBoost) * 50).asInstanceOf[Float]
          startingBoost += 1
        }
      }
      if (numberOfNodes > 15) {
        if ((numberOfNodes - i) <= bottomNodesForNegativeScore) {
          val booster: Float = bottomNodesForNegativeScore.asInstanceOf[Float] - (numberOfNodes - i).asInstanceOf[Float]
          boostScore = -pow(booster, 2.asInstanceOf[Float]).asInstanceOf[Float]
          val negscore: Float = abs(boostScore) + negativeScoring
          if (negscore > 40) {
            boostScore = 5
          }
        }
      }

      trace(logPrefix + "Location Boost Score: " + boostScore + " on interation: " + i + " tag='"+ node.tagName +"' id='" + node.parent.id + "' class='" + node.parent.attr("class"))

      val nodeText: String = node.text
      val wordStats: WordStats = StopWords.getStopWordCount(nodeText, language)
      val upscore: Int = (wordStats.getStopWordCount + boostScore).asInstanceOf[Int]
      updateScore(node.parent, upscore)
      updateScore(node.parent.parent, upscore / 2)
      updateNodeCount(node.parent, 1)
      updateNodeCount(node.parent.parent, 1)
      if (!parentNodes.contains(node.parent)) {
        parentNodes.add(node.parent)
      }
      if (!parentNodes.contains(node.parent.parent)) {
        parentNodes.add(node.parent.parent)
      }

      cnt += 1
      i += 1
    }
    var topNodeScore: Int = 0
    for (e <- parentNodes) {

      trace(logPrefix + "ParentNode: score='" + e.attr("gravityScore") + "' nodeCount='" + e.attr("gravityNodes") + "' id='" + e.id + "' class='" + e.attr("class") + "' ")

      val score: Int = getScore(e)
      println(score)
      if (score > topNodeScore) {
        topNode = e
        topNodeScore = score
      }
      if (topNode == null) {
        topNode = e
      }
    }
    printTraceLog(topNode)
    if (topNode == null) None else Some(topNode)
  }

  def printTraceLog(topNode: Element) {
    try {
      if (topNode != null) {
        trace(logPrefix + "Our TOPNODE: score='" + topNode.attr("gravityScore") + "' nodeCount='" + topNode.attr("gravityNodes") + "' id='" + topNode.id + "' class='" + topNode.attr("class") + "' ")
        val text = if (topNode.text.trim.length > 100) topNode.text.trim.substring(0, 100) + "..." else topNode.text.trim
        trace(logPrefix + "Text - " + text)
      }
    } catch {
      case e: NullPointerException => warn("printTraceLog: " + e.toString)
    }
  }

  /**
  * alot of times the first paragraph might be the caption under an image so we'll want to make sure if we're going to
  * boost a parent node that it should be connected to other paragraphs, at least for the first n paragraphs
  * so we'll want to make sure that the next sibling is a paragraph and has at least some substatial weight to it
  *
  *
  * @param node
  * @return
  */
  private def isOkToBoost(node: Element, language: Language): Boolean = {
    val para = "p"
    var stepsAway: Int = 0
    val minimumStopWordCount = 5
    val maxStepsAwayFromNode = 3

    walkSiblings(node) {
      currentNode => {
        if (currentNode.tagName == para) {
          if (stepsAway >= maxStepsAwayFromNode) {
            trace(logPrefix + "Next paragraph is too far away, not boosting")
            return false
          }
          val paraText: String = currentNode.text
          val wordStats: WordStats = StopWords.getStopWordCount(paraText, language)
          if (wordStats.getStopWordCount > minimumStopWordCount) {
            trace(logPrefix + "We're gonna boost this node, seems contenty")
            return true
          }
          stepsAway += 1
        }
      }
    }
    false
  }

  def getShortText(e: String, max: Int): String = {
    if (e.length > max) e.substring(0, max) + "..." else e
  }

  /**
  * checks the density of links within a node, is there not much text and most of it contains linky shit?
  * if so it's no good
  *
  * @param e
  * @return
  */
  private def isHighLinkDensity(e: Element): Boolean = {
    val links: Elements = e.getElementsByTag("a")
    if (links.size == 0) {
      return false
    }
    val text: String = e.text.trim
    val words: Array[String] = SPACE_SPLITTER.split(text)
    val numberOfWords: Float = words.length
    val sb: StringBuilder = new StringBuilder
    for (link <- links) {
      sb.append(link.text)
    }
    val linkText: String = sb.toString()
    val linkWords: Array[String] = SPACE_SPLITTER.split(linkText)
    val numberOfLinkWords: Float = linkWords.length
    val numberOfLinks: Float = links.size
    val linkDivisor: Float = numberOfLinkWords / numberOfWords
    val score: Float = linkDivisor * numberOfLinks

    trace(logPrefix + "Calculated link density score as: " + score + " for node: " + getShortText(e.text, 50))

    if (score >= 1) {
      return true
    }
    false
  }

  /**
  * returns the gravityScore as an integer from this node
  *
  * @param node
  * @return
  */
  private def getScore(node: Element): Int = {
    getGravityScoreFromNode(node) match {
      case Some(score) => score
      case None => 0
    }
  }

  private def getGravityScoreFromNode(node: Element): Option[Int] = {
    try {
      val grvScoreString: String = node.attr("gravityScore")
      if (string.isNullOrEmpty(grvScoreString)) return None
      Some(Integer.parseInt(grvScoreString))
    } catch {
      case e: Exception => None
    }
  }

  /**
  * adds a score to the gravityScore Attribute we put on divs
  * we'll get the current score then add the score we're passing in to the current
  *
  * @param node
  * @param addToScore - the score to add to the node
  */
  private def updateScore(node: Element, addToScore: Int) {
    var currentScore: Int = 0
    try {
      val scoreString: String = node.attr("gravityScore")
      currentScore = if (string.isNullOrEmpty(scoreString)) 0 else Integer.parseInt(scoreString)
    }
    catch {
      case e: NumberFormatException => {
        currentScore = 0
      }
    }
    val newScore: Int = currentScore + addToScore
    node.attr("gravityScore", Integer.toString(newScore))
  }

  /**
  * stores how many decent nodes are under a parent node
  *
  * @param node
  * @param addToCount
  */
  private def updateNodeCount(node: Element, addToCount: Int) {
    var currentScore: Int = 0
    try {
      val countString: String = node.attr("gravityNodes")
      currentScore = if (string.isNullOrEmpty(countString)) 0 else Integer.parseInt(countString)
    }
    catch {
      case e: NumberFormatException => {
        currentScore = 0
      }
    }
    val newScore: Int = currentScore + addToCount
    node.attr("gravityNodes", Integer.toString(newScore))
  }

  /**
  * pulls out videos we like
  *
  * @return
  */
  def extractVideos(node: Element): List[Element] = {
    val candidates: ArrayList[Element] = new ArrayList[Element]
    val goodMovies = mutable.Buffer[Element]()

    val youtubeStr = "youtube"
    val vimdeoStr = "vimeo"
    val bliptvStr = "blip"
    val flickrStr = "flickr"
    val veohStr = "veoh"
    val dailymotionStr = "dailymotion"

    try {
      node.parent.getElementsByTag("embed").foreach(candidates.add(_))
      node.parent.getElementsByTag("object").foreach(candidates.add(_))

      trace(logPrefix + "extractVideos: Starting to extract videos. Found: " + candidates.size)

      for (el <- candidates) {
        val attrs: Attributes = el.attributes()
        for (a <- attrs) {
          try {
            if ((
                a.getValue.contains(youtubeStr) ||
                a.getValue.contains(bliptvStr) ||
                a.getValue.contains(flickrStr) ||
                a.getValue.contains(veohStr) ||
                a.getValue.contains(dailymotionStr) ||
                a.getValue.contains(vimdeoStr)
              ) && (a.getKey == "src")) {
              trace(logPrefix + "This page has a video!: " + a.getValue)
              goodMovies += el
            }
          }
          catch {
            case e: Exception => {
              info(logPrefix + "Error extracting movies: " + e.toString)
            }
          }
        }
      }
    }
    catch {
      case e: NullPointerException => {
        warn(e.toString, e)
      }
      case e: Exception => {
        warn(e.toString, e)
      }
    }
    trace(logPrefix + "extractVideos:  done looking videos")
    goodMovies.toList
  }

  /**
  * pulls out links we like
  *
  * @return
  */
  def extractLinks(node: Element): Map[String, String] = {
    val goodLinks = mutable.Map[String, String]()

    val candidates = node.parent.select("a[href]").filter(el => el.attr("href") != "#" && !el.attr("abs:href").trim.isEmpty).map(el => goodLinks += el.attr("abs:href") -> el.text)

    trace(logPrefix + "extractLinks: Extracted links. Found: " + candidates.size)

    goodLinks.toMap
  }

  def isTableTagAndNoParagraphsExist(e: Element): Boolean = {

    val subParagraphs: Elements = e.getElementsByTag("p")
    for (p <- subParagraphs) {
      if (p.text.length < 25) {
        p.remove()
      }
    }

    val subParagraphs2: Elements = e.getElementsByTag("p")
    if (subParagraphs2.size == 0 && e.tagName != "td") {
      if (e.tagName == "ul" || e.tagName == "ol") {
        val linkTextLength = e.getElementsByTag("a").map(_.text.length).sum
        val elementTextLength = e.text.length
        if (elementTextLength > 0 && (linkTextLength.toFloat / elementTextLength) < 0.5) {
          return false // less than half of the list is links, so keep this
        }
        trace("List failed link density test: " + linkTextLength + " " + elementTextLength + " " + getShortText(e.text, 50))
      }
      trace("Removing node because it doesn't have any paragraphs " + e.tagName + " " + e.attr("class"))
      return true
    } else {
      return false
    }
  }


  /**
  * remove any divs that looks like non-content, clusters of links, or paras with no gusto
  *
  * @param targetNode
  * @return
  */
  def postExtractionCleanup(targetNode: Element, language: Language): Element = {

    trace(logPrefix + "Starting cleanup Node")
    val node = addSiblings(targetNode, language)
    for {
      e <- node.children
      if (e.tagName != "p" || isHighLinkDensity(e))
    } {
      trace(logPrefix + "CLEANUP  NODE: " + e.id + " class: " + e.attr("class"))
      if (isHighLinkDensity(e) || isTableTagAndNoParagraphsExist(e) || !isNodeScoreThreshholdMet(node, e)) {
        try {
          e.remove()
        } catch {
          case ex: IllegalArgumentException => trace("Cannot remove node: " + ex.toString)
        }
      }
    }
    trace(logPrefix + "Finished cleanup Node")
    node
  }


  def isNodeScoreThreshholdMet(node: Element, e: Element): Boolean = {
    val topNodeScore: Int = getScore(node)
    val currentNodeScore: Int = getScore(e)
    val thresholdScore: Float = (topNodeScore * .08).asInstanceOf[Float]

    trace(logPrefix + "topNodeScore: " + topNodeScore + " currentNodeScore: " + currentNodeScore + " threshold: " + thresholdScore)

    if ((currentNodeScore < thresholdScore) && e.tagName != "td") {
      trace(logPrefix + "Removing node due to low threshold score")
      false
    } else {
      trace(logPrefix + "Not removing TD node")
      true
    }
  }

  /**
  * adds any siblings that may have a decent score to this node
  *
  * @param currentSibling
  * @return
  */
  def getSiblingContent(currentSibling: Element, baselineScoreForSiblingParagraphs: Int, language: Language): Option[String] = {

    if (currentSibling.tagName == "p" && currentSibling.text.length() > 0) {
      Some(currentSibling.outerHtml)

    } else {

      val potentialParagraphs: Elements = currentSibling.getElementsByTag("p")
      if (potentialParagraphs.first == null) {
        None
      } else {

        Some((for {
          firstParagraph <- potentialParagraphs
          if (firstParagraph.text.length() > 0)
          wordStats: WordStats = StopWords.getStopWordCount(firstParagraph.text, language)
          paragraphScore: Int = wordStats.getStopWordCount
          siblingBaseLineScore: Double = .30
          if ((baselineScoreForSiblingParagraphs * siblingBaseLineScore).toDouble < paragraphScore)
        } yield {

          trace(logPrefix + "This node looks like a good sibling, adding it")
          "<p>" + firstParagraph.text + "<p>"

        }).mkString)
      }

    }
  }

  def walkSiblings[T](node: Element)(work: (Element) => T): Seq[T] = {
    var currentSibling: Element = node.previousElementSibling
    val b = mutable.Buffer[T]()

    while (currentSibling != null) {

      trace(logPrefix + "SIBLINGCHECK: " + debugNode(currentSibling))
      b += work(currentSibling)

      currentSibling = if (currentSibling != null) currentSibling.previousElementSibling else null
    }
    b
  }

  private def addSiblings(topNode: Element, language: Language): Element = {

    trace(logPrefix + "Starting to add siblings")

    val baselineScoreForSiblingParagraphs: Int = getBaselineScoreForSiblings(topNode, language)
    val results = walkSiblings(topNode) {
      currentNode => {
        getSiblingContent(currentNode, baselineScoreForSiblingParagraphs, language)

      }
    }.reverse.flatMap(itm => itm)
    topNode.child(0).before(results.mkString)
    topNode
  }

  /**
  * we could have long articles that have tons of paragraphs so if we tried to calculate the base score against
  * the total text score of those paragraphs it would be unfair. So we need to normalize the score based on the average scoring
  * of the paragraphs within the top node. For example if our total score of 10 paragraphs was 1000 but each had an average value of
  * 100 then 100 should be our base.
  *
  * @param topNode
  * @return
  */
  private def getBaselineScoreForSiblings(topNode: Element, language: Language): Int = {
    var base: Int = 100000
    var numberOfParagraphs: Int = 0
    var scoreOfParagraphs: Int = 0
    val nodesToCheck: Elements = topNode.getElementsByTag("p")

    for (node <- nodesToCheck) {
      val nodeText: String = node.text
      val wordStats: WordStats = StopWords.getStopWordCount(nodeText, language)
      val highLinkDensity: Boolean = isHighLinkDensity(node)
      if (wordStats.getStopWordCount > 2 && !highLinkDensity) {
        numberOfParagraphs += 1;
        scoreOfParagraphs += wordStats.getStopWordCount
      }
    }
    if (numberOfParagraphs > 0) {
      base = scoreOfParagraphs / numberOfParagraphs
      if (logger.isDebugEnabled) {
        logger.debug("The base score for siblings to beat is: " + base + " NumOfParas: " + numberOfParagraphs + " scoreOfAll: " + scoreOfParagraphs)
      }
    }
    base
  }

  private def debugNode(e: Element): String = {
    val sb: StringBuilder = new StringBuilder
    sb.append("'GravityScore: '")
    sb.append(e.attr("gravityScore"))
    sb.append("' paraNodeCount: '")
    sb.append(e.attr("gravityNodes"))
    sb.append("' nodeId: '")
    sb.append(e.id)
    sb.append("' className: '")
    sb.append(e.attr("class"))
    sb.toString()
  }
}
