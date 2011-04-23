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

package com.jimplush.goose.cleaners;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Jim Plush
 * Date: 12/18/10
 * This class is used to pre clean documents(webpages)
 * We go through 3 phases of parsing a website cleaning -> extraction -> output formatter
 * This is the cleaning phase that will try to remove comments, known ad junk, social networking divs
 * other things that are known to not be content related.
 */

public class DefaultDocumentCleaner implements DocumentCleaner {
  private static final Logger logger = Logger.getLogger(DefaultDocumentCleaner.class);

  /**
   * this regex is used to remove undesirable nodes from our doc
   * indicate that something maybe isn't content but more of a comment, footer or some other undesirable node
   */
  private static String regExRemoveNodes;

  /**
   * regex to detect if there are block level elements inside of a div element
   */
  private static String divToPElementsRe;

  static {

    divToPElementsRe = "<(a|blockquote|dl|div|img|ol|p|pre|table|ul)";

    StringBuilder sb = new StringBuilder();
    // create negative elements
    sb.append("^side$|combx|retweet|menucontainer|navbar|comment|PopularQuestions|contact|foot|footer|Footer|footnote|cnn_strycaptiontxt|links|meta|scroll|shoutbox|sponsor");
    sb.append("|tags|socialnetworking|socialNetworking|cnnStryHghLght|cnn_stryspcvbx|^inset$|pagetools|post-attributes|welcome_form|contentTools2|the_answers");
    sb.append("|communitypromo|subscribe|vcard|articleheadings|date|print|popup|tools|socialtools|byline|konafilter|KonaFilter|breadcrumbs|^fn$|wp-caption-text");
    regExRemoveNodes = sb.toString();


  }


  public Document clean(Document doc) {

    if (logger.isDebugEnabled()) {
      logger.debug("Starting cleaning phase with DefaultDocumentCleaner");
    }
    Document docToClean = doc;
    docToClean = cleanEmTags(docToClean);
    docToClean = removeDropCaps(docToClean);
    docToClean = removeScriptsAndStyles(docToClean);
    docToClean = cleanBadTags(docToClean);
    docToClean = removeNodesViaRegEx(docToClean, "^caption$");
    docToClean = removeNodesViaRegEx(docToClean, " google ");
    docToClean = removeNodesViaRegEx(docToClean, "^[^entry-]more.*$");

    // remove twitter and facebook nodes, mashable has f'd up class names for this
    docToClean = removeNodesViaRegEx(docToClean, "[^-]facebook");
    docToClean = removeNodesViaRegEx(docToClean, "[^-]twitter");

    // turn any divs that aren't used as true layout items with block level elements inside them into paragraph tags
    docToClean = convertDivsToParagraphs(docToClean, "div");
    docToClean = convertDivsToParagraphs(docToClean, "span");


    return docToClean;
  }

  private Document convertDivsToParagraphs(Document doc, String domType) {

    if (logger.isDebugEnabled()) {
      logger.debug("Starting to replace bad divs...");
    }
    int badDivs = 0;
    int convertedTextNodes = 0;
    Elements divs = doc.getElementsByTag(domType);
    for (Element div : divs) {

      try {

        Pattern pattern = Pattern.compile(this.divToPElementsRe);
        Matcher matcher = pattern.matcher(div.html().toLowerCase());
        boolean matches = matcher.find();
        if (matches == false) {
          Document newDoc = new Document(doc.baseUri());
          Element newNode = newDoc.createElement("p");

          newNode.append(div.html());
          div.replaceWith(newNode);
          badDivs++;

        } else {
          // Try to convert any div with just text inside it to a paragraph so it can be counted as text, otherwise it would be ignored
          // example <div>This is some text in a div</div> should be <div><p>this is some text in a div</p></div>
          //db(div.childNodes().size() + " childnodes");

          //create a master text node to hold all the child node texts so that  links that were replaced with text notes
          //don't become their own paragraphs

          StringBuilder replacementText = new StringBuilder();

          ArrayList<Node> nodesToRemove = new ArrayList<Node>();

          //cleanTags(div);


          for (Node kid : div.childNodes()) {

            if (kid.nodeName().equals("#text")) {


              Node childNode = kid;
              TextNode txtNode = (TextNode) kid;
              String text = txtNode.attr("text");
              //clean up text from tabs and newlines
              text = text.replaceAll("\n", "\n\n");
              text = text.replaceAll("\t", "");
              text = text.replaceAll("^\\s+$", "");

              if (text.length() > 1) {

                // check for siblings that might be links that we want to include in our new node
                Node previousSib = kid.previousSibling();


                if (logger.isDebugEnabled()) {
                  logger.debug("PARENT CLASS: " + div.className() + " NODENAME: " + kid.nodeName());
                  logger.debug("TEXTREPLACE: '" + text.replace("\n", "").replace("<br>", "") + "'");
                }

                if (previousSib != null) {
                  if (previousSib.nodeName().equals("a")) {
                    text = previousSib.outerHtml() + text;
                    if (logger.isDebugEnabled()) {
                      logger.debug("SIBLING NODENAME ADDITION: " + previousSib.nodeName() + " TEXT: " + previousSib.outerHtml());
                    }
                  }
                }

                replacementText.append(text);
                nodesToRemove.add(kid);

                convertedTextNodes++;
              }

            }


          }

          // replace div's text with the new master replacement text node that containts the sum of all the little text nodes
          //div.appendChild(replacementTextNode);

          Document newDoc = new Document(doc.baseUri());
          Element newPara = newDoc.createElement("p");
          newPara.html(replacementText.toString());

          div.appendChild(newPara);
          newDoc = null;


          for (Node n : nodesToRemove) {
            n.remove();
          }


        }
      } catch (NullPointerException e) {
        logger.error(e.toString());
      }

    }

    if (logger.isDebugEnabled()) {
      logger.debug("Found " + divs.size() + " total divs with " + badDivs + " bad divs replaced and " + convertedTextNodes + " textnodes converted inside divs");
    }


    return doc;
  }

  private Document removeScriptsAndStyles(Document doc) {

    if (logger.isDebugEnabled()) {
      logger.debug("Starting to remove script tags");
    }
    Elements scripts = doc.getElementsByTag("script");
    for (Element item : scripts) {
      item.remove();
    }
    if (logger.isDebugEnabled()) {
      logger.debug("Removed: " + scripts.size() + " script tags");
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Removing Style Tags");
    }
    Elements styles = doc.getElementsByTag("style");
    for (Element style : styles) {
      style.remove();
    }

    return doc;


  }

  /**
   * replaces <em> tags with textnodes
   */
  private Document cleanEmTags(Document doc) {
    Elements ems = doc.getElementsByTag("em");
    if (logger.isDebugEnabled()) {
      logger.debug("Cleaning " + ems.size() + " EM tags");
    }
    for (Element node : ems) {
      // replace the node with a div node
      Elements images = node.getElementsByTag("img");
      if (images.size() != 0) {
        continue;
      }
      TextNode tn = new TextNode(node.text(), doc.baseUri());
      node.replaceWith(tn);
    }
    return doc;
  }


  /**
   * remove those css drop caps where they put the first letter in big text in the 1st paragraph
   */
  private Document removeDropCaps(Document doc) {
    Elements items = doc.select("span[class~=(dropcap|drop_cap)]");
    if (logger.isDebugEnabled()) {
      logger.debug("Cleaning " + items.size() + " dropcap tags");
    }
    for (Element item : items) {
      TextNode tn = new TextNode(item.text(), doc.baseUri());
      item.replaceWith(tn);
    }
    return doc;
  }


  private Document cleanBadTags(Document doc) {

    Elements naughtyList = doc.select("[id~=(" + this.regExRemoveNodes + ")]");
    if (logger.isDebugEnabled()) {
      logger.debug(naughtyList.size() + " naughty ID elements found");
    }
    for (Element node : naughtyList) {
      if (logger.isDebugEnabled()) {
        logger.debug("Cleaning: Removing node with class: id: " + node.id());
      }
      node.remove();
    }
    Elements naughtyList2 = doc.select("[id~=(" + this.regExRemoveNodes + ")]");
    if (logger.isDebugEnabled()) {
      logger.debug(naughtyList2.size() + " naughty ID elements found after removal");
    }

    Elements naughtyList3 = doc.select("[class~=(" + this.regExRemoveNodes + ")]");
    if (logger.isDebugEnabled()) {
      logger.debug(naughtyList3.size() + " naughty CLASS elements found");
    }
    for (Element node : naughtyList3) {
      if (logger.isDebugEnabled()) {
        logger.debug("clean: Removing node with class: " + node.className());
      }
      node.remove();
    }
    Elements naughtyList4 = doc.select("[class~=(" + this.regExRemoveNodes + ")]");
    if (logger.isDebugEnabled()) {
      logger.debug(naughtyList4.size() + " naughty CLASS elements found after removal");
    }

    // starmagazine puts shit on name tags instead of class or id
    Elements naughtyList5 = doc.select("[name~=(" + this.regExRemoveNodes + ")]");
    if (logger.isDebugEnabled()) {
      logger.debug(naughtyList5.size() + " naughty Name elements found");
    }
    for (Element node : naughtyList5) {
      if (logger.isDebugEnabled()) {
        logger.debug("clean: Removing node with class: " + node.attr("class") + " id: " + node.id() + " name: " + node.attr("name"));
      }
      node.remove();
    }

    return doc;

  }

  /**
   * removes nodes that may have a certain pattern that matches against a class or id tag
   *
   * @param pattern
   */
  private Document removeNodesViaRegEx(Document doc, String pattern) {
    try {

      Elements naughtyList = doc.getElementsByAttributeValueMatching("id", pattern);
      if (logger.isDebugEnabled()) {
        logger.debug("regExRemoveNodes: " + naughtyList.size() + " ID elements found against pattern: " + pattern);
      }
      for (Element node : naughtyList) {
        node.remove();
      }

      Elements naughtyList3 = doc.getElementsByAttributeValueMatching("class", pattern);
      if (logger.isDebugEnabled()) {
        logger.debug("regExRemoveNodes: " + naughtyList3.size() + " CLASS elements found against pattern: " + pattern);
      }
      for (Element node : naughtyList3) {
        node.remove();
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      logger.error(e.toString());
    }
    return doc;
  }

}
