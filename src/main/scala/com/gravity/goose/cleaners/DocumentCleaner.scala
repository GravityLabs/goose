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
import org.jsoup.select.Elements
import java.util.regex.{Matcher, Pattern}
import java.util.ArrayList
import org.jsoup.nodes.{TextNode, Node, Element, Document}
import com.gravity.goose.text.{string, ReplaceSequence}
import scala.collection.JavaConversions._
import com.gravity.goose.Article

trait DocumentCleaner extends Logging {

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

    trace(logPrefix + "Starting cleaning phase with DefaultDocumentCleaner")

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
    docToClean = convertDivsToParagraphs(docToClean, "div")
    docToClean = convertDivsToParagraphs(docToClean, "span")
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
    trace(logPrefix + ems.size + " EM tags modified")
    doc
  }

  /**
  * remove those css drop caps where they put the first letter in big text in the 1st paragraph
  */
  private def removeDropCaps(doc: Document): Document = {
    val items: Elements = doc.select("span[class~=(dropcap|drop_cap)]")
    trace(logPrefix + items.size + " dropcap tags removed")
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
    trace(logPrefix + scripts.size + " script tags removed")

    val styles: Elements = doc.getElementsByTag("style")
    import scala.collection.JavaConversions._
    for (style <- styles) {
      style.remove()
    }
    trace(logPrefix + styles.size + " style tags removed")
    doc
  }

  private def cleanBadTags(doc: Document): Document = {
    val children: Elements = doc.body.children
    val naughtyList: Elements = children.select(queryNaughtyIDs)
    trace(logPrefix + naughtyList.size + " naughty ID elements found")

    import scala.collection.JavaConversions._
    for (node <- naughtyList) {
      trace(logPrefix + "Removing node with id: " + node.id)
      removeNode(node)
    }

    val naughtyList2: Elements = children.select(queryNaughtyIDs)
    trace(logPrefix + naughtyList2.size + " naughty ID elements found after removal")

    val naughtyClasses: Elements = children.select(queryNaughtyClasses)

    trace(logPrefix + naughtyClasses.size + " naughty CLASS elements found")

    for (node <- naughtyClasses) {
      trace(logPrefix + "Removing node with class: " + node.className)
      removeNode(node)
    }

    val naughtyClasses2: Elements = children.select(queryNaughtyClasses)
    trace(logPrefix + naughtyClasses2.size + " naughty CLASS elements found after removal")

    val naughtyList5: Elements = children.select(queryNaughtyNames)

    trace(logPrefix + naughtyList5.size + " naughty Name elements found")

    for (node <- naughtyList5) {

      trace(logPrefix + "Removing node with class: " + node.attr("class") + " id: " + node.id + " name: " + node.attr("name"))

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

      trace(logPrefix + naughtyList.size + " ID elements found against pattern: " + pattern)

      for (node <- naughtyList) {
        removeNode(node)
      }
      val naughtyList3: Elements = doc.getElementsByAttributeValueMatching("class", pattern)
      trace(logPrefix + naughtyList3.size + " CLASS elements found against pattern: " + pattern)

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


  private def convertDivsToParagraphs(doc: Document, domType: String): Document = {
    trace(logPrefix + "Starting to replace bad divs...")
    var badDivs: Int = 0
    var convertedTextNodes: Int = 0
    val divs: Elements = doc.getElementsByTag(domType)

    for (div <- divs) {
      try {
        val divToPElementsMatcher: Matcher = divToPElementsPattern.matcher(div.html.toLowerCase)
        if (divToPElementsMatcher.find == false) {
          val newDoc: Document = new Document(doc.baseUri)
          val newNode: Element = newDoc.createElement("p")
          newNode.append(div.html)
          div.replaceWith(newNode)
          ({
            badDivs += 1;
            badDivs
          })
        }
        else {
          val replacementText: StringBuilder = new StringBuilder
          val nodesToRemove: ArrayList[Node] = new ArrayList[Node]
          for {
            kid <- div.childNodes;
            if (kid.nodeName == "#text");
            txtNode: TextNode = kid.asInstanceOf[TextNode];
            text: String = txtNode.attr("text");
            if (!string.isNullOrEmpty(text))
          } {
            val replaceText = tabsAndNewLinesReplcesments.replaceAll(text)
            if (replaceText.length > 1) {
              val previousSib: Node = kid.previousSibling

              trace(logPrefix + "PARENT CLASS: " + div.className + " NODENAME: " + kid.nodeName)
              trace(logPrefix + "TEXTREPLACE: '" + replaceText.replace("\n", "").replace("<br>", "") + "'")

              if (previousSib != null) {
                if (previousSib.nodeName == "a") {
                  replacementText.append(previousSib.outerHtml)

                  trace(logPrefix + "SIBLING NODENAME ADDITION: " + previousSib.nodeName + " TEXT: " + previousSib.outerHtml)
                }
              }
              replacementText.append(replaceText)
              nodesToRemove.add(kid)
              ({
                convertedTextNodes += 1;
                convertedTextNodes
              })
            }

          }
          var newDoc: Document = new Document(doc.baseUri)
          val newPara: Element = newDoc.createElement("p")
          newPara.html(replacementText.toString())
          div.childNode(0).before(newPara.outerHtml)
          newDoc = null
          import scala.collection.JavaConversions._
          for (n <- nodesToRemove) {
            n.remove()
          }
        }
      }
      catch {
        case e: NullPointerException => {
          logger.error(e.toString)
        }
      }
    }

    trace(logPrefix + "Found %d total %s with %d bad ones replaced and %d textnodes converted inside %s"
        .format(divs.size, domType, badDivs, convertedTextNodes, domType))

    doc
  }



}


object DocumentCleaner {
  /**
  * this regex is used to remove undesirable nodes from our doc
  * indicate that something maybe isn't content but more of a comment, footer or some other undesirable node
  */
  var regExRemoveNodes: String = null
  var queryNaughtyIDs: String = null
  var queryNaughtyClasses: String = null
  var queryNaughtyNames: String = null
  var tabsAndNewLinesReplcesments: ReplaceSequence = null
  /**
  * regex to detect if there are block level elements inside of a div element
  */
  val divToPElementsPattern: Pattern = Pattern.compile("<(a|blockquote|dl|div|img|ol|p|pre|table|ul)")

  val captionPattern: Pattern = Pattern.compile("^caption$")
  val googlePattern: Pattern = Pattern.compile(" google ")
  val entriesPattern: Pattern = Pattern.compile("^[^entry-]more.*$")
  val facebookPattern: Pattern = Pattern.compile("[^-]facebook")
  val twitterPattern: Pattern = Pattern.compile("[^-]twitter")

  val logPrefix = "Cleaner: "
  var sb: StringBuilder = new StringBuilder

  // create negative elements
  sb.append("^side$|combx|retweet|menucontainer|navbar|comment|PopularQuestions|contact|foot|footer|Footer|footnote|cnn_strycaptiontxt|links|meta$|scroll|shoutbox|sponsor")
  sb.append("|tags|socialnetworking|socialNetworking|cnnStryHghLght|cnn_stryspcvbx|^inset$|pagetools|post-attributes|welcome_form|contentTools2|the_answers")
  sb.append("|communitypromo|subscribe|vcard|articleheadings|date|print|popup|author-dropdown|tools|socialtools|byline|konafilter|KonaFilter|breadcrumbs|^fn$|wp-caption-text")

  regExRemoveNodes = sb.toString()
  queryNaughtyIDs = "[id~=(" + regExRemoveNodes + ")]"
  queryNaughtyClasses = "[class~=(" + regExRemoveNodes + ")]"
  queryNaughtyNames = "[name~=(" + regExRemoveNodes + ")]"

  tabsAndNewLinesReplcesments = ReplaceSequence.create("\n", "\n\n").append("\t").append("^\\s+$")

}


