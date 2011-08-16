package com.gravity.goose

import utils.Logging
import org.jsoup.nodes.Document

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/14/11
 */

class Article extends Logging {

  /**
  * title of the article
  */
  var title: String = null

  /**
  * meta description field in HTML source
  */
  var metaDescription: String = null

  /**
  * meta keywords field in the HTML source
  */
  var metaKeywords: String = null

  /**
  * The canonical link of this article if found in the meta data
  */
  var canonicalLink: String = null

  /**
  * holds the domain of this article we're parsing
  */
  var domain: String = null


  /**
  * stores the final URL that we're going to try and fetch content against, this would be expanded if any
  * escaped fragments were found in the starting url
  */
  var finalUrl: String = null;

  /**
  * stores the RAW HTML straight from the network connection
  */
  var rawHtml: String = null

  /**
  * the JSoup Document object
  */
  var doc: Document = null

  /**
  * this is the original JSoup document that contains a pure object from the original HTML without any cleaning
  * options done on it
  */
  var rawDoc: Document = null

  def testLogging() {
    info("hi there")
  }


}