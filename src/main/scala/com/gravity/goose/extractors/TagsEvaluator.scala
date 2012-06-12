package org.jsoup.select

import org.jsoup.nodes.Element

/**
 * Created by IntelliJ IDEA.
 * Author: Robbie Coleman
 * Date: 6/12/12
 * Time: 12:04 PM
 */

class TagsEvaluator(tags: scala.collection.Set[String]) extends Evaluator {
  def matches(root: Element, element: Element) = tags.contains(element.tagName())
}

object TagsEvaluator {
  def apply(tags: String*): TagsEvaluator = new TagsEvaluator(tags.toSet)
}
