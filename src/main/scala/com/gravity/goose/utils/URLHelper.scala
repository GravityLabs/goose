package com.gravity.goose.utils

import com.gravity.goose.text.{StringReplacement, HashUtils}
import java.net.{MalformedURLException, URL}

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/14/11
 */

case class ParsingCandidate(urlString: String, linkhash: String, url: URL)

object URLHelper extends Logging {

  private val ESCAPED_FRAGMENT_REPLACEMENT: StringReplacement = StringReplacement.compile("#!", "?_escaped_fragment_=")

  /**
  * returns a ParseCandidate object  that is a valid URL
  */
  def getCleanedUrl(urlToCrawl: String): Option[ParsingCandidate] = {

    val finalURL =
      if (urlToCrawl.contains("#!")) ESCAPED_FRAGMENT_REPLACEMENT.replaceAll(urlToCrawl) else urlToCrawl

    try {
      val url = new URL(urlToCrawl)
      val linkhash = HashUtils.md5(urlToCrawl)
      Some(ParsingCandidate(finalURL, linkhash, url))
    }
    catch {
      case e: MalformedURLException => {
        warn("%s - is a malformed URL and cannot be processed")
        None
      }
    }
  }
}