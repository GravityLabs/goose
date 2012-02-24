package com.gravity.goose.extractors

import com.gravity.goose.Article
import com.gravity.goose.text.string

/**
 * Created by IntelliJ IDEA.
 * Author: Robbie Coleman
 * Date: 2/13/12
 * Time: 11:31 AM
 */

class VoicesContentExtractor extends ContentExtractor {
  override def getTitle(article: Article): String = {
    try {
      val titleElem = article.doc.getElementsByTag("title")
      if (titleElem == null || titleElem.isEmpty) return string.empty
      
      titleElem.first().text() match {
        case mt if (string.isNullOrEmpty(mt)) => string.empty
        case titleText => {
          val pieces = DASH_SPLITTER.split(titleText)
          val titlePiece = pieces(0)
          if (string.isNullOrEmpty(titlePiece)) return string.empty
          TITLE_REPLACEMENTS.replaceAll(titlePiece)
        }
      }
    } catch {
      case ex: Exception => {
        getLogger().warn(ex.toString)
        string.empty
      }
    }
  }
}
