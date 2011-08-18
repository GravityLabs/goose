package com.gravity.goose.images

import com.gravity.goose.utils.Logging
import org.jsoup.nodes.{Element, Document}

/**
* Created by Jim Plush
* User: jim
* Date: 8/18/11
*/
trait ImageExtractor extends Logging {

  val logPrefix = "images: "

  def getBestImage(doc: Document, topNode: Element): Image
}

