package com.jimplush.goose.util;

import org.jsoup.nodes.Element;

public class JsoupUtils {
  
  /**
   * Apparently jsoup expects the node's parent to not be null and throws if it is. Let's be safe.
   * @param node the node to remove from the doc
   */
  public static void removeNode(Element node) {
    if (node == null || node.parent() == null) return;
    node.remove();
  }
}
