package com.jimplush.goose;

import com.jimplush.goose.network.HtmlFetcher;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: jim plush
 * Date: 12/16/10
 */


public class ContentExtractor {
  private static final Logger logger = Logger.getLogger(ContentExtractor.class);

  /**
   * holds our article class that will be the results of this crawl
   */
  private Article article = new Article();

  /**
   * holds our jSoup document object that represents the DOM
   */
  private Document doc;

  public Article extractContent(String urlToCrawl)
  {
    try {
      URL url = new URL(urlToCrawl);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Invalid URL Passed in: "+urlToCrawl, e);

    }

    ParseWrapper parseWrapper = new ParseWrapper();

    String rawHtml = HtmlFetcher.getHtml(urlToCrawl);





    return article;
  }

}
