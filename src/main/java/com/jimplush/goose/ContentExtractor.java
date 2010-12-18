package com.jimplush.goose;

import com.jimplush.goose.cleaners.DefaultDocumentCleaner;
import com.jimplush.goose.cleaners.DocumentCleaner;
import com.jimplush.goose.network.HtmlFetcher;
import com.jimplush.goose.network.MaxBytesException;
import com.jimplush.goose.network.NotHtmlException;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: jim plush
 * Date: 12/16/10
 * a lot of work in this class is based on Arc90's readability code that does content extraction in JS
 * I wasn't able to find a good server side codebase to acheive the same so I started with their base ideas and then
 * built additional metrics on top of it.
 */


public class ContentExtractor {
  private static final Logger logger = Logger.getLogger(ContentExtractor.class);

  private DocumentCleaner documentCleaner;

  public Article extractContent(String urlToCrawl) {

    try {
      URL url = new URL(urlToCrawl);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Invalid URL Passed in: " + urlToCrawl, e);
    }

    ParseWrapper parseWrapper = new ParseWrapper();
    Article article;
    try {
      String rawHtml = HtmlFetcher.getHtml(urlToCrawl);
      Document doc = parseWrapper.parse(rawHtml, urlToCrawl);

      DocumentCleaner documentCleaner = getDocCleaner();
      doc = documentCleaner.clean(doc);

      article = new Article();

      article.setTitle(this.fetchTitle(doc, article));


    } catch (MaxBytesException e) {
      throw new RuntimeException(e);
    } catch (NotHtmlException e) {
      throw new RuntimeException(e);
    }


    return article;
  }

  /**
   * todo allow for setter to override the default documentCleaner in case user wants more flexibility
   * @return
   */
  private DocumentCleaner getDocCleaner() {
    if(this.documentCleaner == null ) {
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
  private String fetchTitle(Document doc, Article article) {
    String title = "";

    return title;

  }

}
