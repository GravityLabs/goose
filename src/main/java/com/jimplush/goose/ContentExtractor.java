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
package com.jimplush.goose;

import com.jimplush.goose.cleaners.DefaultDocumentCleaner;
import com.jimplush.goose.cleaners.DocumentCleaner;
import com.jimplush.goose.images.BestImageGuesser;
import com.jimplush.goose.images.ImageExtractor;
import com.jimplush.goose.network.HtmlFetcher;
import com.jimplush.goose.network.MaxBytesException;
import com.jimplush.goose.network.NotHtmlException;
import com.jimplush.goose.outputformatters.DefaultOutputFormatter;
import com.jimplush.goose.outputformatters.OutputFormatter;
import com.jimplush.goose.texthelpers.HashUtils;
import com.jimplush.goose.texthelpers.StopWords;
import com.jimplush.goose.texthelpers.WordStats;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * User: jim plush
 * Date: 12/16/10
 * a lot of work in this class is based on Arc90's readability code that does content extraction in JS
 * I wasn't able to find a good server side codebase to acheive the same so I started with their base ideas and then
 * built additional metrics on top of it such as looking for clusters of english stopwords.
 * Gravity was doing 30+ million links per day with this codebase across a series of crawling servers for a project
 * and it held up well. Our current port is slightly different than this one but I'm working to align them so the goose
 * project gets the love as we continue to move forward.
 */


public class ContentExtractor {


  // PRIVATE PROPERTIES BELOW

  private static final Logger logger = LoggerFactory.getLogger(ContentExtractor.class);

  /**
   * holds the configuration settings we want to use
   */
  private Configuration config;

  // sets the default cleaner class to prep the HTML for parsing
  private DocumentCleaner documentCleaner;


  // the MD5 of the URL we're currently parsing, used to references the images we download to the url so we
  // can more easily clean up resources when we're done with the page.
  private String linkhash;


  // once we have our topNode then we want to format that guy for output to the user
  private OutputFormatter outputFormatter;

  private ImageExtractor imageExtractor;


  /**
   * you can optionally pass in a configuration object here that will allow you to override the settings
   * that goose comes default with
   */
  public ContentExtractor() {
    this.config = new Configuration();
  }

  /**
   * overloaded to accept a custom configuration object
   *
   * @param config
   */
  public ContentExtractor(Configuration config) {
    this.config = config;
  }


  public Article extractContent(String urlToCrawl) {

    urlToCrawl = getUrlToCrawl(urlToCrawl);
    try {
      URL url = new URL(urlToCrawl);

      this.linkhash = HashUtils.md5(urlToCrawl);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Invalid URL Passed in: " + urlToCrawl, e);
    }

    ParseWrapper parseWrapper = new ParseWrapper();
    Article article = null;
    try {
      String rawHtml = HtmlFetcher.getHtml(urlToCrawl);
      Document doc = parseWrapper.parse(rawHtml, urlToCrawl);

      DocumentCleaner documentCleaner = getDocCleaner();
      doc = documentCleaner.clean(doc);


      article = new Article();

      article.setTitle(getTitle(doc, article));
      article.setMetaDescription(getMetaDescription(doc));
      article.setMetaKeywords(getMetaKeywords(doc));
      article.setCanonicalLink(getCanonicalLink(doc, urlToCrawl));
      article.setDomain(article.getCanonicalLink());


      // extract the content of the article
      article.setTopNode(calculateBestNodeBasedOnClustering(doc));

      if (article.getTopNode() != null) {

        // extract any movie embeds out from our main article content
        article.setMovies(extractVideos(article.getTopNode()));


        if (config.isEnableImageFetching()) {
          HttpClient httpClient = HtmlFetcher.getHttpClient();
          imageExtractor = getImageExtractor(httpClient, urlToCrawl);
          article.setTopImage(imageExtractor.getBestImage(doc, article.getTopNode()));

        }

        // grab siblings and remove high link density elements
        cleanupNode(article.getTopNode());


        outputFormatter = getOutputFormatter();
        outputFormatter.getFormattedElement(article.getTopNode());


        article.setCleanedArticleText(outputFormatter.getFormattedText());


        if (logger.isDebugEnabled()) {
          logger.debug("FINAL EXTRACTION TEXT: \n" + article.getCleanedArticleText());
        }

        if (config.isEnableImageFetching()) {
          if (logger.isDebugEnabled()) {
            logger.debug("\n\nFINAL EXTRACTION IMAGE: \n" + article.getTopImage().getImageSrc());
          }
        }

      }

      // cleans up all the temp images that we've downloaded
      releaseResources();


    } catch (MaxBytesException e) {
      logger.error(e.toString(), e);
    } catch (NotHtmlException e) {
      logger.error("URL: " + urlToCrawl + " did not contain valid HTML to parse, exiting. " + e.toString());
    } catch (Exception e) {
      logger.error("General Exception occured on url: " + urlToCrawl + " " + e.toString());
    }


    return article;
  }

  // used for gawker type ajax sites with pound sites
  private String getUrlToCrawl(String urlToCrawl) {
    String finalURL = urlToCrawl;
    if (urlToCrawl.contains("#!")) {
      finalURL = urlToCrawl.replace("#!", "?_escaped_fragment_=");
    }

    if (logger.isInfoEnabled()) {
      logger.info("Goose is about to crawl URL: " + finalURL);
    }

    return finalURL;
  }


  // todo create a setter for this for people to override output formatter
  private OutputFormatter getOutputFormatter() {
    if (outputFormatter == null) {
      return new DefaultOutputFormatter();
    } else {
      return outputFormatter;
    }

  }


  private ImageExtractor getImageExtractor(HttpClient httpClient, String urlToCrawl) {

    if (imageExtractor == null) {
      BestImageGuesser bestImageGuesser = new BestImageGuesser(this.config, httpClient, urlToCrawl);
      return bestImageGuesser;
    } else {
      return imageExtractor;
    }

  }

  /**
   * todo allow for setter to override the default documentCleaner in case user wants more flexibility
   *
   * @return
   */
  private DocumentCleaner getDocCleaner() {
    if (this.documentCleaner == null) {
      return new DefaultDocumentCleaner();
    }
    return this.documentCleaner;
  }

  /**
   * attemps to grab titles from the html pages, lots of sites use different delimiters
   * for titles so we'll try and do our best guess.
   *
   * @param doc
   * @param article
   * @return
   */
  private String getTitle(Document doc, Article article) {
    String title = "";

    try {

      String titleText = "";

      titleText = doc.getElementsByTag("title").first().text();

      boolean usedDelimeter = false;

      if (titleText.contains("|")) {
        titleText = doTitleSplits(titleText, "\\|");
        usedDelimeter = true;
      }

      if (titleText.contains("-") && !usedDelimeter) {
        titleText = doTitleSplits(titleText, " - ");
        usedDelimeter = true;
      }
      if (titleText.contains("»") && !usedDelimeter) {
        titleText = doTitleSplits(titleText, "»");
        usedDelimeter = true;
      }

      if (titleText.contains(":") && !usedDelimeter) {
        titleText = doTitleSplits(titleText, ":");
        usedDelimeter = true;
      }

      // encode unicode charz
      title = StringEscapeUtils.escapeHtml(titleText);
      ;

      // todo this is a hack until I can fix this.. weird motely crue error with
      // http://money.cnn.com/2010/10/25/news/companies/motley_crue_bp.fortune/index.htm?section=money_latest
      title = title.replace("&#65533;", "");

      if (logger.isDebugEnabled()) {
        logger.debug("Page title is: " + title);
      }

    } catch (NullPointerException e) {
      logger.error(e.toString());
    }
    return title;

  }

  /**
   * based on a delimeter in the title take the longest piece or do some custom logic based on the site
   *
   * @param title
   * @param delimeter
   * @return
   */
  private String doTitleSplits(String title, String delimeter) {

    String largeText = "";
    int largetTextLen = 0;

    String[] titlePieces = title.split(delimeter);

    // take the largest split
    for (String p : titlePieces) {
      if (p.length() > largetTextLen) {
        largeText = p;
        largetTextLen = p.length();
      }
    }

    largeText = largeText.replace("&raquo;", "");
    largeText = largeText.replace("»", "");


    return largeText.trim();


  }


  /**
   * if the article has meta description set in the source, use that
   */
  private String getMetaDescription(Document doc) {
    String metaDescription = "";
    Elements meta = doc.select("meta[name=description]");
    if (meta.size() > 0) {
      metaDescription = meta.first().attr("content").trim();
    }
    return metaDescription;
  }

  /**
   * if the article has meta keywords set in the source, use that
   */
  private String getMetaKeywords(Document doc) {
    String metaKeywords = "";
    Elements meta = doc.select("meta[name=keywords]");
    if (meta.size() > 0) {
      metaKeywords = meta.first().attr("content").trim();
    }
    return metaKeywords;
  }

  /**
   * if the article has meta canonical link set in the url
   */
  private String getCanonicalLink(Document doc, String baseUrl) {
    String canonicalUrl = baseUrl;
    Elements meta = doc.select("link[rel=canonical]");
    if (meta.size() > 0) {
      canonicalUrl = meta.first().attr("href").trim();

    }

    // set domain based on canonicalUrl
    URL url = null;
    try {

      if (canonicalUrl != null) {
        if (!canonicalUrl.startsWith("http://")) {
          url = new URL(new URL(baseUrl), canonicalUrl);
        } else {
          url = new URL(canonicalUrl);
        }

      } else {
        url = new URL(baseUrl);
      }

    } catch (MalformedURLException e) {
      logger.error(e.toString(), e);
    }
    return canonicalUrl;


  }

  private String getDomain(String canonicalLink) {
    URL url = null;
    try {
      url = new URL(canonicalLink);
      String domain = url.getHost();
      return domain;
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }

  }

  /**
   * we're going to start looking for where the clusters of paragraphs are. We'll score a cluster based on the number of stopwords
   * and the number of consecutive paragraphs together, which should form the cluster of text that this node is around
   * also store on how high up the paragraphs are, comments are usually at the bottom and should get a lower score
   *
   * @return
   */
  private Element calculateBestNodeBasedOnClustering(Document doc) {
    Element topNode = null;

    // grab all the paragraph elements on the page to start to inspect the likely hood of them being good peeps
    ArrayList<Element> nodesToCheck = getNodesToCheck(doc);

    double startingBoost = 1.0;
    int cnt = 0;
    int i = 0;

    // holds all the parents of the nodes we're checking
    Set<Element> parentNodes = new HashSet<Element>();


    ArrayList<Element> nodesWithText = new ArrayList<Element>();


    for (Element node : nodesToCheck) {

      String nodeText = node.text();
      WordStats wordStats = StopWords.getStopWordCount(nodeText);
      boolean highLinkDensity = isHighLinkDensity(node);


      if (wordStats.getStopWordCount() > 2 && !highLinkDensity) {

        nodesWithText.add(node);
      }

    }

    int numberOfNodes = nodesWithText.size();
    int negativeScoring = 0; // we shouldn't give more negatives than positives
    // we want to give the last 20% of nodes negative scores in case they're comments
    double bottomNodesForNegativeScore = (float) numberOfNodes * 0.25;

    if (logger.isDebugEnabled()) {
      logger.debug("About to inspect num of nodes with text: " + numberOfNodes);
    }

    for (Element node : nodesWithText) {

      // add parents and grandparents to scoring
      // only add boost to the middle paragraphs, top and bottom is usually jankz city
      // so basically what we're doing is giving boost scores to paragraphs that appear higher up in the dom
      // and giving lower, even negative scores to those who appear lower which could be commenty stuff

      float boostScore = 0;

      if (isOkToBoost(node, i)) {
        if (cnt >= 0) {
          boostScore = (float) ((1.0 / startingBoost) * 50);
          startingBoost++;
        }
      }


      // check for negative node values
      if (numberOfNodes > 15) {
        if ((numberOfNodes - i) <= bottomNodesForNegativeScore) {
          float booster = (float) bottomNodesForNegativeScore - (float) (numberOfNodes - i);
          boostScore = -(float) Math.pow(booster, (float) 2);

          // we don't want to score too highly on the negative side.
          float negscore = Math.abs(boostScore) + negativeScoring;
          if (negscore > 40) {
            boostScore = 5;
          }
        }
      }

      if (logger.isDebugEnabled()) {
        logger.debug("Location Boost Score: " + boostScore + " on interation: " + i + "' id='" + node.parent().id() + "' class='" + node.parent().attr("class"));
      }
      String nodeText = node.text();
      WordStats wordStats = StopWords.getStopWordCount(nodeText);
      int upscore = (int) (wordStats.getStopWordCount() + boostScore);
      updateScore(node.parent(), upscore);
      updateScore(node.parent().parent(), upscore / 2);
      updateNodeCount(node.parent(), 1);
      updateNodeCount(node.parent().parent(), 1);

      if (!parentNodes.contains(node.parent())) {
        parentNodes.add(node.parent());
      }

      if (!parentNodes.contains(node.parent().parent())) {
        parentNodes.add(node.parent().parent());
      }

      cnt++;
      i++;
    }


    // now let's find the parent node who scored the highest

    int topNodeScore = 0;
    for (Element e : parentNodes) {

      if (logger.isDebugEnabled()) {
        logger.debug("ParentNode: score='" + e.attr("gravityScore") + "' nodeCount='" + e.attr("gravityNodes") + "' id='" + e.id() + "' class='" + e.attr("class") + "' ");
      }
      //int score = Integer.parseInt(e.attr("gravityScore")) * Integer.parseInt(e.attr("gravityNodes"));
      int score = getScore(e);
      if (score > topNodeScore) {
        topNode = e;
        topNodeScore = score;
      }

      if (topNode == null) {
        topNode = e;
      }
    }
    if (topNode == null) {
      if (logger.isDebugEnabled()) {
        logger.debug("ARTICLE NOT ABLE TO BE EXTRACTED!, WE HAZ FAILED YOU LORD VADAR");
      }
    } else {
      String logText = "";
      String targetText = "";
      Element topPara = topNode.getElementsByTag("p").first();
      if (topPara == null) {
        topNode.text();
      } else {
        topPara.text();
      }

      if (targetText.length() >= 51) {
        logText = targetText.substring(0, 50);
      } else {
        logText = targetText;
      }
      if (logger.isDebugEnabled()) {
        logger.debug("TOPNODE TEXT: " + logText.trim());
        logger.debug("Our TOPNODE: score='" + topNode.attr("gravityScore") + "' nodeCount='" + topNode.attr("gravityNodes") + "' id='" + topNode.id() + "' class='" + topNode.attr("class") + "' ");
      }
    }


    return topNode;


  }

  /**
   * returns a list of nodes we want to search on like paragraphs and tables
   *
   * @return
   */
  private ArrayList<Element> getNodesToCheck(Document doc) {
    ArrayList<Element> nodesToCheck = new ArrayList<Element>();

    Elements items = doc.getElementsByTag("p");
    for (Element item : items) {
      nodesToCheck.add(item);
    }
    Elements items2 = doc.getElementsByTag("pre");
    for (Element item : items2) {
      nodesToCheck.add(item);
    }
    Elements items3 = doc.getElementsByTag("td");
    for (Element item : items3) {
      nodesToCheck.add(item);
    }
    return nodesToCheck;

  }

  /**
   * checks the density of links within a node, is there not much text and most of it contains linky shit?
   * if so it's no good
   *
   * @param e
   * @return
   */
  private static boolean isHighLinkDensity(Element e) {

    Elements links = e.getElementsByTag("a");
    String txt = e.text().trim();

    if (links.size() == 0) {
      return false;
    }

    float score = 0;
    String text = e.text();
    String[] words = text.split(" ");
    float numberOfWords = words.length;


    // let's loop through all the links and calculate the number of words that make up the links
    StringBuilder sb = new StringBuilder();
    for (Element link : links) {
      sb.append(link.text());
    }
    String linkText = sb.toString();
    String[] linkWords = linkText.split(" ");
    float numberOfLinkWords = linkWords.length;

    float numberOfLinks = links.size();

    float linkDivisor = (float) (numberOfLinkWords / numberOfWords);
    score = (float) linkDivisor * numberOfLinks;

    String logText = "";
    if (e.text().length() >= 51) {
      logText = e.text().substring(0, 50);
    } else {
      logText = e.text();
    }
    if (logger.isDebugEnabled()) {
      logger.debug("Calulated link density score as: " + score + " for node: " + logText);
    }
    if (score > 1) {
      return true;
    }

    return false;
  }

  /**
   * alot of times the first paragraph might be the caption under an image so we'll want to make sure if we're going to
   * boost a parent node that it should be connected to other paragraphs, at least for the first n paragraphs
   * so we'll want to make sure that the next sibling is a paragraph and has at least some substatial weight to it
   *
   * @param node
   * @param i
   * @return
   */
  private boolean isOkToBoost(Element node, int i) {

    int stepsAway = 0;

    Element sibling = node.nextElementSibling();
    while (sibling != null) {

      if (sibling.tagName().equals("p")) {
        if (stepsAway >= 3) {
          if (logger.isDebugEnabled()) {
            logger.debug("Next paragraph is too far away, not boosting");
          }
          return false;
        }

        String paraText = sibling.text();
        String html = sibling.outerHtml();
        WordStats wordStats = StopWords.getStopWordCount(paraText);
        if (wordStats.getStopWordCount() > 5) {
          if (logger.isDebugEnabled()) {
            logger.debug("We're gonna boost this node, seems contenty");
          }
          return true;
        }

      }

      // increase how far away the next paragraph is from this node
      stepsAway++;

      sibling = sibling.nextElementSibling();
    }


    return false;
  }


  /**
   * adds a score to the gravityScore Attribute we put on divs
   * we'll get the current score then add the score we're passing in to the current
   *
   * @param node
   * @param addToScore - the score to add to the node
   */
  private void updateScore(Element node, int addToScore) {
    int currentScore;
    try {
      currentScore = Integer.parseInt(node.attr("gravityScore"));
    } catch (NumberFormatException e) {
      currentScore = 0;
    }
    int newScore = currentScore + addToScore;
    node.attr("gravityScore", Integer.toString(newScore));

  }

  /**
   * stores how many decent nodes are under a parent node
   *
   * @param node
   * @param addToCount
   */
  private void updateNodeCount(Element node, int addToCount) {
    int currentScore;
    try {
      currentScore = Integer.parseInt(node.attr("gravityNodes"));
    } catch (NumberFormatException e) {
      currentScore = 0;
    }
    int newScore = currentScore + addToCount;
    node.attr("gravityNodes", Integer.toString(newScore));

  }


  /**
   * returns the gravityScore as an integer from this node
   *
   * @param node
   * @return
   */
  private int getScore(Element node) {
    try {
      return Integer.parseInt(node.attr("gravityScore"));
    } catch (NumberFormatException e) {
      return 0;
    } catch (NullPointerException e) {
      return 0;
    }
  }


  /**
   * pulls out videos we like
   *
   * @return
   */
  private ArrayList<Element> extractVideos(Element node) {
    ArrayList<Element> candidates = new ArrayList<Element>();
    ArrayList<Element> goodMovies = new ArrayList<Element>();
    try {


      Elements embeds = node.parent().getElementsByTag("embed");
      for (Element el : embeds) {
        candidates.add(el);
      }
      Elements objects = node.parent().getElementsByTag("object");
      for (Element el : objects) {
        candidates.add(el);
      }
      if (logger.isDebugEnabled()) {
        logger.debug("extractVideos: Starting to extract videos. Found: " + candidates.size());
      }

      for (Element el : candidates) {

        Attributes attrs = el.attributes();

        for (Attribute a : attrs) {
          try {
            if (logger.isDebugEnabled()) {
              logger.debug(a.getKey() + " : " + a.getValue());
            }
            if ((a.getValue().contains("youtube") || a.getValue().contains("vimeo")) && a.getKey().equals("src")) {
              if (logger.isDebugEnabled()) {
                logger.debug("Found video... setting");
                logger.debug("This page has a video!: " + a.getValue());
              }
              goodMovies.add(el);

            }
          } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
          }
        }

      }
    } catch (NullPointerException e) {
      logger.error(e.toString(), e);
    } catch (Exception e) {
      logger.error(e.toString(), e);
    }
    if (logger.isDebugEnabled()) {
      logger.debug("extractVideos:  done looking videos");
    }
    return goodMovies;
  }


  /**
   * remove any divs that looks like non-content, clusters of links, or paras with no gusto
   *
   * @param node
   * @return
   */
  private Element cleanupNode(Element node) {
    if (logger.isDebugEnabled()) {
      logger.debug("Starting cleanup Node");
    }

    node = addSiblings(node);

    Elements nodes = node.children();
    for (Element e : nodes) {
      if (e.tagName().equals("p")) {
        continue;
      }
      if (logger.isDebugEnabled()) {
        logger.debug("CLEANUP  NODE: " + e.id() + " class: " + e.attr("class"));
      }
      boolean highLinkDensity = isHighLinkDensity(e);
      if (highLinkDensity) {
        if (logger.isDebugEnabled()) {
          logger.debug("REMOVING  NODE FOR LINK DENSITY: " + e.id() + " class: " + e.attr("class"));
        }
        e.remove();
        continue;
      }
      // now check for word density
      // grab all the paragraphs in the children and remove ones that are too small to matter
      Elements subParagraphs = e.getElementsByTag("p");


      for (Element p : subParagraphs) {
        if (p.text().length() < 25) {
          p.remove();
        }
      }

      // now that we've removed shorty paragraphs let's make sure to exclude any first paragraphs that don't have paras as
      // their next siblings to avoid getting img bylines
      // first let's remove any element that now doesn't have any p tags at all
      Elements subParagraphs2 = e.getElementsByTag("p");
      if (subParagraphs2.size() == 0 && !e.tagName().equals("td")) {
        if (logger.isDebugEnabled()) {
          logger.debug("Removing node because it doesn't have any paragraphs");
        }
        e.remove();
        continue;
      }

      //if this node has a decent enough gravityScore we should keep it as well, might be content
      int topNodeScore = getScore(node);
      int currentNodeScore = getScore(e);
      float thresholdScore = (float) (topNodeScore * .08);
      if (logger.isDebugEnabled()) {
        logger.debug("topNodeScore: " + topNodeScore + " currentNodeScore: " + currentNodeScore + " threshold: " + thresholdScore);
      }
      if (currentNodeScore < thresholdScore) {
        if (!e.tagName().equals("td")) {
          if (logger.isDebugEnabled()) {
            logger.debug("Removing node due to low threshold score");
          }
          e.remove();
        } else {
          if (logger.isDebugEnabled()) {
            logger.debug("Not removing TD node");
          }
        }

        continue;
      }

    }

    return node;

  }


  /**
   * adds any siblings that may have a decent score to this node
   *
   * @param node
   * @return
   */
  private Element addSiblings(Element node) {
    if (logger.isDebugEnabled()) {
      logger.debug("Starting to add siblings");
    }
    int baselineScoreForSiblingParagraphs = getBaselineScoreForSiblings(node);

    Element currentSibling = node.previousElementSibling();
    while (currentSibling != null) {
      if (logger.isDebugEnabled()) {
        logger.debug("SIBLINGCHECK: " + debugNode(currentSibling));
      }

      if (currentSibling.tagName().equals("p")) {

        node.child(0).before(currentSibling.outerHtml());
        currentSibling = currentSibling.previousElementSibling();
        continue;
      }

      // check for a paraph embedded in a containing element
      int insertedSiblings = 0;
      Elements potentialParagraphs = currentSibling.getElementsByTag("p");
      if (potentialParagraphs.first() == null) {
        currentSibling = currentSibling.previousElementSibling();
        continue;
      }
      for (Element firstParagraph : potentialParagraphs) {
        WordStats wordStats = StopWords.getStopWordCount(firstParagraph.text());

        int paragraphScore = wordStats.getStopWordCount();

        if ((float) (baselineScoreForSiblingParagraphs * .30) < paragraphScore) {
          if (logger.isDebugEnabled()) {
            logger.debug("This node looks like a good sibling, adding it");
          }
          node.child(insertedSiblings).before("<p>" + firstParagraph.text() + "<p>");
          insertedSiblings++;
        }

      }

      currentSibling = currentSibling.previousElementSibling();
    }
    return node;


  }

  /**
   * we could have long articles that have tons of paragraphs so if we tried to calculate the base score against
   * the total text score of those paragraphs it would be unfair. So we need to normalize the score based on the average scoring
   * of the paragraphs within the top node. For example if our total score of 10 paragraphs was 1000 but each had an average value of
   * 100 then 100 should be our base.
   *
   * @param topNode
   * @return
   */
  private int getBaselineScoreForSiblings(Element topNode) {

    int base = 100000;

    int numberOfParagraphs = 0;
    int scoreOfParagraphs = 0;

    Elements nodesToCheck = topNode.getElementsByTag("p");

    for (Element node : nodesToCheck) {

      String nodeText = node.text();
      WordStats wordStats = StopWords.getStopWordCount(nodeText);
      boolean highLinkDensity = isHighLinkDensity(node);


      if (wordStats.getStopWordCount() > 2 && !highLinkDensity) {

        numberOfParagraphs++;
        scoreOfParagraphs += wordStats.getStopWordCount();
      }

    }

    if (numberOfParagraphs > 0) {
      base = scoreOfParagraphs / numberOfParagraphs;
      if (logger.isDebugEnabled()) {
        logger.debug("The base score for siblings to beat is: " + base + " NumOfParas: " + numberOfParagraphs + " scoreOfAll: " + scoreOfParagraphs);
      }
    }

    return base;


  }

  private String debugNode(Element e) {

    StringBuilder sb = new StringBuilder();
    sb.append("GravityScore: '");
    sb.append(e.attr("gravityScore"));
    sb.append("' paraNodeCount: '");
    sb.append(e.attr("gravityNodes"));
    sb.append("' nodeId: '");
    sb.append(e.id());
    sb.append("' className: '");
    sb.append(e.attr("class"));
    return sb.toString();

  }

  /**
   * cleans up any temp shit we have laying around like temp images
   * removes any image in the temp dir that starts with the linkhash of the url we just parsed
   */
  public void releaseResources() {
    if (logger.isDebugEnabled()) {
      logger.debug("STARTING TO RELEASE ALL RESOURCES");
    }
    File dir = new File(config.getLocalStoragePath());
    String[] children = dir.list();

    if (children == null) {
      logger.debug("No Temp images found for linkhash: " + this.linkhash);
    } else {
      for (int i = 0; i < children.length; i++) {
        // Get filename of file or directory
        String filename = children[i];

        if (filename.startsWith(this.linkhash)) {

          File f = new File(dir.getAbsolutePath() + "/" + filename);
          if (!f.delete()) {
            logger.error("Unable to remove temp file: " + filename);
          }
        }
      }
    }

  }


}
