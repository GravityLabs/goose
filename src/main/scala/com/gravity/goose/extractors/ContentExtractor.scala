package com.gravity.goose.extractors

import com.gravity.goose.Article
import org.jsoup.select.Elements
import com.gravity.goose.text._
import com.gravity.goose.utils.Logging
import org.apache.commons.lang.StringEscapeUtils
import org.jsoup.nodes.Document

/**
* Created by Jim Plush
* User: jim
* Date: 8/15/11
*/

trait ContentExtractor extends Logging {
  // PRIVATE PROPERTIES BELOW

  val MOTLEY_REPLACEMENT: StringReplacement = StringReplacement.compile("&#65533;", string.empty)
  val ESCAPED_FRAGMENT_REPLACEMENT: StringReplacement = StringReplacement.compile("#!", "?_escaped_fragment_=")
  val TITLE_REPLACEMENTS: ReplaceSequence = ReplaceSequence.create("&raquo;").append("»")
  val PIPE_SPLITTER: StringSplitter = new StringSplitter("\\|")
  val DASH_SPLITTER: StringSplitter = new StringSplitter(" - ")
  val ARROWS_SPLITTER: StringSplitter = new StringSplitter("»")
  val COLON_SPLITTER: StringSplitter = new StringSplitter(":")
  val SPACE_SPLITTER: StringSplitter = new StringSplitter(" ")
  //   val NO_STRINGS: Set[String] = new HashSet[String](0)
  val A_REL_TAG_SELECTOR: String = "a[rel=tag], a[href*=/tag/]"

  def getTitle(article: Article): String = {
    var title: String = string.empty

    val doc = article.doc

    try {
      var titleElem: Elements = doc.getElementsByTag("title")
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

      trace("Page title is: " + title)

      title

    }
    catch {
      case e: NullPointerException => {
        warn(e.toString);
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
    var largetTextLen: Int = 0
    var largeTextIndex: Int = 0
    var titlePieces: Array[String] = splitter.split(title)
    var i: Int = 0
    while (i < titlePieces.length) {

      var current: String = titlePieces(i)
      if (current.length > largetTextLen) {
        largetTextLen = current.length
        largeTextIndex = i
      }
      i += 1

    }
    TITLE_REPLACEMENTS.replaceAll(titlePieces(largeTextIndex)).trim
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
    getMetaContent(article.doc, "meta[name=description]")
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
    val meta = article.doc.select("link[rel=canonical]");
    if (meta.size() > 0) {
      val href = meta.first().attr("href");
      if (string.isNullOrEmpty(href)) string.empty else href.trim()
    } else {
      article.finalUrl;
    }
  }
}