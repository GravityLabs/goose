package com.jimplush.goose.outputformatters; /**
 * User: jim
 * Date: 12/19/10
 */

import com.jimplush.goose.texthelpers.StopWords;
import com.jimplush.goose.texthelpers.WordStats;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * this class will be responsible for taking our top node and stripping out junk we don't want
 * and getting it ready for how we want it presented to the user
 */
public class DefaultOutputFormatter implements OutputFormatter {
  private static final Logger logger = Logger.getLogger(DefaultOutputFormatter.class);

  private Element topNode;

  public Element getFormattedElement(Element topNode) {

    this.topNode = topNode;

    removeNodesWithNegativeScores();

    convertLinksToText();

    replaceTagsWithText();

    removeParagraphsWithFewWords();

    return topNode;

  }

  /**
   * takes an element and turns the P tags into \n\n
   * // todo move this to an output formatter object instead of inline here
   *
   * @return
   */
  public String getFormattedText() {

    StringBuilder sb = new StringBuilder();

    Elements nodes = topNode.getAllElements();
    for (Element e : nodes) {
      if (e.tagName().equals("p")) {
        String text = StringEscapeUtils.escapeHtml(e.text()).trim();
        sb.append(text);
        sb.append("\n\n");
      }
    }

    return sb.toString();
  }
  
  /**
   * Same as getFormattedText but we don't escape HTML 
   * */
  public String getUnescapedFormattedText(){

    StringBuilder sb = new StringBuilder();

    Elements nodes = topNode.getAllElements();
    for (Element e : nodes) {
      if (e.tagName().equals("p")) {
       
        sb.append(e.text());
        sb.append("\n\n");
      }
    }

    return sb.toString();
	  
  }

  /**
   * cleans up and converts any nodes that should be considered text into text
   */
  private void convertLinksToText()
  {
    logger.info("Turning links to text");
    Elements links = topNode.getElementsByTag("a");
    for(Element item: links) {
      if(item.getElementsByTag("img").size() == 0) {
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
    for(Element item: strongs) {
      TextNode tn = new TextNode(item.text(), topNode.baseUri());
      item.replaceWith(tn);
    }
    
    Elements bolds = topNode.getElementsByTag("b");
    for(Element item: bolds) {
      TextNode tn = new TextNode(item.text(), topNode.baseUri());
      item.replaceWith(tn);
    }
    
    Elements italics = topNode.getElementsByTag("i");
    for(Element item: italics) {
      TextNode tn = new TextNode(item.text(), topNode.baseUri());
      item.replaceWith(tn);
    }
  }

  /**
   * remove paragraphs that have less than x number of words, would indicate that it's some sort of link
   */
  private void removeParagraphsWithFewWords() {
    logger.info("removeParagraphsWithFewWords starting...");

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
