package com.gravity.goose.extractors

import org.jsoup.nodes.Element

/**
* Created by IntelliJ IDEA.
* User: robbie
* Date: 5/19/11
* Time: 2:45 PM
*/
/**
* Encapsulates the process of extracting some type <code>T</code> from an article
* @param <T> the type of {@link Object} the implementing class will return
*/
trait Extractor[T] {
  /**
  * Given the specified {@link Element}, extract @param <T>
  *
  * @param rootElement passed in from the {@link com.jimplush.goose.ContentExtractor} after the article has been parsed
  * @return an instance of type <code>T</code>
  */
  def extract(rootElement: Element): T
}