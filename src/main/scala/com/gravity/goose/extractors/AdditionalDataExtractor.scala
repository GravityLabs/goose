package com.gravity.goose.extractors

import org.jsoup.nodes.Element
import java.util.Map

/**
* Implement this abstract class to extract anything not currently contained within the {@link com.jimplush.goose.Article} class
*/
class AdditionalDataExtractor extends Extractor[Map[String, String]] {
  def extract(rootElement: Element): Map[String, String] = {
    null
  }
}



