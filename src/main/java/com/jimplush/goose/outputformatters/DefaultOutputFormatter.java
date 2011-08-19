/**
 * Licensed to Gravity.com under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Gravity.com licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jimplush.goose.outputformatters; /**
 * User: jim plush
 * Date: 12/19/10
 */

import com.jimplush.goose.texthelpers.StopWords;
import com.jimplush.goose.texthelpers.WordStats;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this class will be responsible for taking our top node and stripping out junk we don't want
 * and getting it ready for how we want it presented to the user
 */
public class DefaultOutputFormatter implements OutputFormatter {
  private static final Logger logger = LoggerFactory.getLogger(DefaultOutputFormatter.class);

  private Element topNode;

  /**
   * Depricated use {@link #getFormattedText(Element)}
   * @param topNode the top most node to format
   * @return the prepared Element
   */
  @Deprecated
  public Element getFormattedElement(Element topNode) {

    this.topNode = topNode;

    removeNodesWithNegativeScores();

    convertLinksToText();

    replaceTagsWithText();

    removeParagraphsWithFewWords();

    return topNode;

  }

  /**
   * Removes all unnecessarry elements and formats the selected text nodes
   * @param topNode the top most node to format
   * @return a formatted string with all HTML removed
   */
  public String getFormattedText(Element topNode) {

    this.topNode = topNode;

    removeNodesWithNegativeScores();

    convertLinksToText();

    replaceTagsWithText();

    removeParagraphsWithFewWords();

    return getFormattedText();
  }

  /**
   * Depricated use {@link #getFormattedText(Element)}
   * takes an element and turns the P tags into \n\n
   * // todo move this to an output formatter object instead of inline here
   *
   * @return
   */
  @Deprecated
  public String getFormattedText() {

    StringBuilder sb = new StringBuilder();

    Elements nodes = topNode.getAllElements();
    for (Element e : nodes) {
      if (e.tagName().equals("p")) {
        String text = StringEscapeUtils.unescapeHtml(e.text()).trim();
        sb.append(text);
        sb.append("\n\n");
      }
    }

    return sb.toString();
  }

  /**
   * cleans up and converts any nodes that should be considered text into text
   */
  private void convertLinksToText() {
    if (logger.isDebugEnabled()) {
      logger.debug("Turning links to text");
    }
    Elements links = topNode.getElementsByTag("a");
    for (Element item : links) {
      if (item.getElementsByTag("img").size() == 0) {
        TextNode tn = new TextNode(item.text(), topNode.baseUri());
        item.replaceWith(tn);
      }
    }
  }

  /**
   * if there are elements inside our top node that have a negative gravity score, let's
   * give em the boot
   */
  private void removeNodesWithNegativeScores() {
    Elements gravityItems = this.topNode.select("*[gravityScore]");
    for (Element item : gravityItems) {
      int score = Integer.parseInt(item.attr("gravityScore"));
      if (score < 1) {
        item.remove();
      }
    }
  }

  /**
   * replace common tags with just text so we don't have any crazy formatting issues
   * so replace <br>, <i>, <strong>, etc.... with whatever text is inside them
   */
  private void replaceTagsWithText() {

    Elements strongs = topNode.getElementsByTag("strong");
    for (Element item : strongs) {
      TextNode tn = new TextNode(item.text(), topNode.baseUri());
      item.replaceWith(tn);
    }

    Elements bolds = topNode.getElementsByTag("b");
    for (Element item : bolds) {
      TextNode tn = new TextNode(item.text(), topNode.baseUri());
      item.replaceWith(tn);
    }

    Elements italics = topNode.getElementsByTag("i");
    for (Element item : italics) {
      TextNode tn = new TextNode(item.text(), topNode.baseUri());
      item.replaceWith(tn);
    }
  }

  /**
   * remove paragraphs that have less than x number of words, would indicate that it's some sort of link
   */
  private void removeParagraphsWithFewWords() {
    if (logger.isDebugEnabled()) {
      logger.debug("removeParagraphsWithFewWords starting...");
    }

    Elements allNodes = this.topNode.getAllElements();
    for (Element el : allNodes) {

      try {
        // get stop words that appear in each node

        WordStats stopWords = StopWords.getStopWordCount(el.text());

        if (stopWords.getStopWordCount() < 5 && el.getElementsByTag("object").size() == 0 && el.getElementsByTag("embed").size() == 0) {
          el.remove();
        }
      } catch (IllegalArgumentException e) {
        logger.error(e.getMessage());
      }
      //}
    }
  }

}
