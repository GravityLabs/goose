package com.gravity.goose

import images.Image
import utils.Logging
import org.jsoup.nodes.{Element, Document}

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
  * stores the lovely, pure text from the article, stripped of html, formatting, etc...
  * just raw text with paragraphs separated by newlines. This is probably what you want to use.
  */
  var cleanedArticleText: String = ""

  /**
  * meta description field in HTML source
  */
  var metaDescription: String = ""

  /**
  * meta keywords field in the HTML source
  */
  var metaKeywords: String = ""

  /**
  * The canonical link of this article if found in the meta data
  */
  var canonicalLink: String = ""

  /**
  * holds the domain of this article we're parsing
  */
  var domain: String = ""

  /**
  * holds the top Element we think is a candidate for the main body of the article
  */
  var topNode: Element = null

  /**
  * holds the top Image object that we think represents this article
  */
  var topImage: Image = new Image


  /**
  * holds a set of tags that may have been in the artcle, these are not meta keywords
  */
  var tags: Set[String] = null

  /**
  * holds a list of any movies we found on the page like youtube, vimeo
  */
  var movies: List[Element] = Nil

  /**
  * stores the final URL that we're going to try and fetch content against, this would be expanded if any
  * escaped fragments were found in the starting url
  */
  var finalUrl: String = null;

  /**
  * stores the MD5 hash of the url to use for various identification tasks
  */
  var linkhash: String = null;

  /**
  * stores the RAW HTML straight from the network connection
  */
  var rawHtml: String = ""

  /**
  * the JSoup Document object
  */
  var doc: Document = null

  /**
  * this is the original JSoup document that contains a pure object from the original HTML without any cleaning
  * options done on it
  */
  var rawDoc: Document = null


}