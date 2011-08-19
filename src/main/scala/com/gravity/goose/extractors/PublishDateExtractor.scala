package com.gravity.goose.extractors

import org.jsoup.nodes.Element
import java.util.Date

/**
* Implement this class to extract the {@link Date} of when this article was published.
*/
/**
 * Created by IntelliJ IDEA.
 * User: robbie
 * Date: 5/19/11
 * Time: 2:50 PM
 */
abstract class PublishDateExtractor extends Extractor[Date] {
  /**
  * Intended to search the DOM and identify the {@link Date} of when this article was published.
  * <p>This will be called by the {@link com.jimplush.goose.ContentExtractor#extractContent(String)} method and will be passed to {@link com.jimplush.goose.Article#setPublishDate(java.util.Date)}</p>
  *
  * @param rootElement passed in from the {@link com.jimplush.goose.ContentExtractor} after the article has been parsed
  * @return {@link Date} of when this particular article was published or <code>null</code> if no date could be found.
  */
  def extract(rootElement: Element): Date
}



