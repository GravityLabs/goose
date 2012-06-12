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
package com.gravity.goose.cleaners

import com.gravity.goose.utils.Logging
import java.util.regex.{Matcher, Pattern}
import org.jsoup.nodes.{TextNode, Node, Element, Document}
import com.gravity.goose.text.ReplaceSequence
import scala.collection.JavaConversions._
import com.gravity.goose.Article
import collection.mutable.ListBuffer
import org.jsoup.select.{TagsEvaluator, Collector, Elements}

trait DocumentCleaner {

  /**
  * User: Jim Plush
  * Date: 12/18/10
  * This class is used to pre clean documents(webpages)
  * We go through 3 phases of parsing a website cleaning -> extraction -> output formatter
  * This is the cleaning phase that will try to remove comments, known ad junk, social networking divs
  * other things that are known to not be content related.
  */

  import DocumentCleaner._


  def clean(article: Article): Document = {

    trace("Starting cleaning phase with DefaultDocumentCleaner")

    var docToClean: Document = article.doc
    docToClean = cleanEmTags(docToClean)
    docToClean = removeDropCaps(docToClean)
    docToClean = removeScriptsAndStyles(docToClean)
    docToClean = cleanBadTags(docToClean)
    docToClean = removeNodesViaRegEx(docToClean, captionPattern)
    docToClean = removeNodesViaRegEx(docToClean, googlePattern)
    docToClean = removeNodesViaRegEx(docToClean, entriesPattern)
    docToClean = removeNodesViaRegEx(docToClean, facebookPattern)
    docToClean = removeNodesViaRegEx(docToClean, twitterPattern)
    docToClean = cleanUpSpanTagsInParagraphs(docToClean)
    docToClean = convertWantedTagsToParagraphs(docToClean, articleRootTags)
//    docToClean = convertDivsToParagraphs(docToClean, "div")
//    docToClean = convertDivsToParagraphs(docToClean, "span")

    //    docToClean = convertDivsToParagraphs(docToClean, "span")
    docToClean
  }

  /**
  * replaces <em> tags with textnodes
  */
  private def cleanEmTags(doc: Document): Document = {
    val ems: Elements = doc.getElementsByTag("em")

    for {
      node <- ems
      images: Elements = node.getElementsByTag("img")
      if (images.size == 0)
    } {
      val tn: TextNode = new TextNode(node.text, doc.baseUri)
      node.replaceWith(tn)
    }
    trace(ems.size + " EM tags modified")
    doc
  }

  /**
  * takes care of the situation where you have a span tag nested in a paragraph tag
  * e.g. businessweek2.txt
  */
  private def cleanUpSpanTagsInParagraphs(doc: Document) = {
    val spans: Elements = doc.getElementsByTag("span")
    for (item <- spans) {
      if (item.parent().nodeName() == "p") {
        val tn: TextNode = new TextNode(item.text, doc.baseUri)
        item.replaceWith(tn)
        trace("Replacing nested span with TextNode: " + item.text())
      }
    }
    doc
  }

  /**
  * remove those css drop caps where they put the first letter in big text in the 1st paragraph
  */
  private def removeDropCaps(doc: Document): Document = {
    val items: Elements = doc.select("span[class~=(dropcap|drop_cap)]")
    trace(items.size + " dropcap tags removed")
    for (item <- items) {
      val tn: TextNode = new TextNode(item.text, doc.baseUri)
      item.replaceWith(tn)
    }
    doc
  }


  private def removeScriptsAndStyles(doc: Document): Document = {

    val scripts: Elements = doc.getElementsByTag("script")
    for (item <- scripts) {
      item.remove()
    }
    trace(scripts.size + " script tags removed")

    val styles: Elements = doc.getElementsByTag("style")
    import scala.collection.JavaConversions._
    for (style <- styles) {
      style.remove()
    }
    trace(styles.size + " style tags removed")
    doc
  }

  private def cleanBadTags(doc: Document): Document = {
    val children: Elements = doc.body.children
    val naughtyList: Elements = children.select(queryNaughtyIDs)
    trace(naughtyList.size + " naughty ID elements found")

    import scala.collection.JavaConversions._
    for (node <- naughtyList) {
      trace("Removing node with id: " + node.id)
      removeNode(node)
    }

    val naughtyList2: Elements = children.select(queryNaughtyIDs)
    trace(naughtyList2.size + " naughty ID elements found after removal")

    val naughtyClasses: Elements = children.select(queryNaughtyClasses)

    trace(naughtyClasses.size + " naughty CLASS elements found")

    for (node <- naughtyClasses) {
      trace("Removing node with class: " + node.className)
      removeNode(node)
    }

    val naughtyClasses2: Elements = children.select(queryNaughtyClasses)
    trace(naughtyClasses2.size + " naughty CLASS elements found after removal")

    val naughtyList5: Elements = children.select(queryNaughtyNames)

    trace(naughtyList5.size + " naughty Name elements found")

    for (node <- naughtyList5) {

      trace("Removing node with class: " + node.attr("class") + " id: " + node.id + " name: " + node.attr("name"))

      removeNode(node)
    }
    doc
  }


  /**
  * removes nodes that may have a certain pattern that matches against a class or id tag
  *
  * @param pattern
  */
  private def removeNodesViaRegEx(doc: Document, pattern: Pattern): Document = {
    try {
      val naughtyList: Elements = doc.getElementsByAttributeValueMatching("id", pattern)

      trace(naughtyList.size + " ID elements found against pattern: " + pattern)

      for (node <- naughtyList) {
        removeNode(node)
      }
      val naughtyList3: Elements = doc.getElementsByAttributeValueMatching("class", pattern)
      trace(naughtyList3.size + " CLASS elements found against pattern: " + pattern)

      for (node <- naughtyList3) {
        removeNode(node)
      }
    }
    catch {
      case e: IllegalArgumentException => {
        warn(e, e.toString)
      }
    }
    doc
  }

  /**
  * Apparently jsoup expects the node's parent to not be null and throws if it is. Let's be safe.
  * @param node the node to remove from the doc
  */
  private def removeNode(node: Element) {
    if (node == null || node.parent == null) return
    node.remove()
  }


  def replaceElementsWithPara(doc: Document, div: Element) {
    val newDoc: Document = new Document(doc.baseUri)
    val newNode: Element = newDoc.createElement("p")
    newNode.append(div.html)
    div.replaceWith(newNode)
  }

  private def convertWantedTagsToParagraphs(doc: Document, wantedTags: TagsEvaluator): Document = {

    val selected = Collector.collect(wantedTags, doc)

    for (elem <- selected) {
      if (Collector.collect(blockElemementTags, elem).isEmpty) {
        replaceElementsWithPara(doc, elem)
      } else {
        val replacements = getReplacementNodes(doc, elem)
        elem.children().foreach(_.remove())
        replacements.foreach(n => {
          try {
            elem.appendChild(n)
          } catch {
            case ex: Exception => info(ex, "Failed to append cleaned child!")
          }
        })
      }
    }

    doc
  }


  private def convertDivsToParagraphs(doc: Document, domType: String): Document = {
    trace("Starting to replace bad divs...")
    var badDivs: Int = 0
    var convertedTextNodes: Int = 0
    val divs: Elements = doc.getElementsByTag(domType)
    var divIndex = 0


    for (div <- divs) {
      try {
        val divToPElementsMatcher: Matcher = divToPElementsPattern.matcher(div.html.toLowerCase)
        if (divToPElementsMatcher.find == false) {
          replaceElementsWithPara(doc, div)
          badDivs += 1;
        }
        else {
          val replaceNodes = getReplacementNodes(doc, div)

          div.children().foreach(_.remove())
          replaceNodes.foreach(node => {

            try {
              div.appendChild(node)
            } catch {
              case e: Exception => info(e, e.toString)
            }

          })
        }
      }
      catch {
        case e: NullPointerException => {
          logger.error(e.toString)
        }
      }
      divIndex += 1
    }

    trace("Found %d total %s with %d bad ones replaced and %d textnodes converted inside %s"
        .format(divs.size, domType, badDivs, convertedTextNodes, domType))


    doc
  }

  /**
  * go through all the div's nodes and clean up dangling text nodes and get rid of obvious jank
  */
  def getFlushedBuffer(replacementText: scala.StringBuilder, doc: Document) = {
    val bufferedText = replacementText.toString()
    trace("Flushing TextNode Buffer: " + bufferedText.trim())
    val newDoc: Document = new Document(doc.baseUri)
    val newPara: Element = newDoc.createElement("p")
    newPara.html(replacementText.toString())
    newPara
  }

  def getReplacementNodes(doc: Document, div: Element) = {

    val replacementText: StringBuilder = new StringBuilder
    val nodesToReturn = new ListBuffer[Node]()

    val nodesToRemove = new ListBuffer[Node]()

    for {

      kid <- div.childNodes()
    } {


      if (kid.nodeName() == "p" && replacementText.size > 0) {

        // flush the buffer of text
        val newNode = getFlushedBuffer(replacementText, doc)
        nodesToReturn += newNode
        replacementText.clear()

        if (kid.isInstanceOf[Element]) {
          val kidElem = kid.asInstanceOf[Element]
          nodesToReturn += kidElem
        }


      } else if (kid.nodeName == "#text") {


        val kidTextNode = kid.asInstanceOf[TextNode]
        val kidText = kidTextNode.attr("text")
        val replaceText = tabsAndNewLinesReplacements.replaceAll(kidText)
        if (replaceText.trim().length > 1) {

          var prevSibNode = kidTextNode.previousSibling()
          while (prevSibNode != null && prevSibNode.nodeName() == "a" && prevSibNode.attr("grv-usedalready") != "yes") {
            replacementText.append(" " + prevSibNode.outerHtml() + " ")
            nodesToRemove += prevSibNode
            prevSibNode.attr("grv-usedalready", "yes")
            prevSibNode = if (prevSibNode.previousSibling() == null) null else prevSibNode.previousSibling()
          }
          // add the text of the node
          replacementText.append(replaceText)

          //          check the next set of links that might be after text (see businessinsider2.txt)
          var nextSibNode = kidTextNode.nextSibling()
          while (nextSibNode != null && nextSibNode.nodeName() == "a" && nextSibNode.attr("grv-usedalready") != "yes") {
            replacementText.append(" " + nextSibNode.outerHtml() + " ")
            nodesToRemove += nextSibNode
            nextSibNode.attr("grv-usedalready", "yes")
            nextSibNode = if (nextSibNode.nextSibling() == null) null else nextSibNode.nextSibling()
          }


        }
        nodesToRemove += kid

      } else {
        nodesToReturn += kid
      }


    }
    // flush out anything still remaining
    if (replacementText.size > 0) {
      val newNode = getFlushedBuffer(replacementText, doc)
      nodesToReturn += newNode
      replacementText.clear()
    }


    nodesToRemove.foreach(_.remove())
    nodesToReturn

  }


}


object DocumentCleaner extends Logging {
  var sb: StringBuilder = new StringBuilder

  // create negative elements
  sb.append("^side$|combx|retweet|mediaarticlerelated|menucontainer|navbar|comment|PopularQuestions|contact|foot|footer|Footer|footnote|cnn_strycaptiontxt|links|meta$|scroll|shoutbox|sponsor")
  sb.append("|tags|socialnetworking|socialNetworking|cnnStryHghLght|cnn_stryspcvbx|^inset$|pagetools|post-attributes|welcome_form|contentTools2|the_answers|remember-tool-tip")
  sb.append("|communitypromo|runaroundLeft|subscribe|vcard|articleheadings|date|^print$|popup|author-dropdown|tools|socialtools|byline|konafilter|KonaFilter|breadcrumbs|^fn$|wp-caption-text")

  /**
  * this regex is used to remove undesirable nodes from our doc
  * indicate that something maybe isn't content but more of a comment, footer or some other undesirable node
  */
  val regExRemoveNodes = sb.toString()
  val queryNaughtyIDs = "[id~=(" + regExRemoveNodes + ")]"
  val queryNaughtyClasses = "[class~=(" + regExRemoveNodes + ")]"
  val queryNaughtyNames = "[name~=(" + regExRemoveNodes + ")]"
  val tabsAndNewLinesReplacements = ReplaceSequence.create("\n", "\n\n").append("\t").append("^\\s+$")
  /**
  * regex to detect if there are block level elements inside of a div element
  */
  val divToPElementsPattern: Pattern = Pattern.compile("<(a|blockquote|dl|div|img|ol|p|pre|table|ul)")

  val blockElemementTags = TagsEvaluator("a", "blockquote", "dl", "div", "img", "ol", "p", "pre", "table", "ul")
  val articleRootTags = TagsEvaluator("div", "span", "article")

  val captionPattern: Pattern = Pattern.compile("^caption$")
  val googlePattern: Pattern = Pattern.compile(" google ")
  val entriesPattern: Pattern = Pattern.compile("^[^entry-]more.*$")
  val facebookPattern: Pattern = Pattern.compile("[^-]facebook")
  val twitterPattern: Pattern = Pattern.compile("[^-]twitter")

  val logPrefix = "Cleaner: "

}


