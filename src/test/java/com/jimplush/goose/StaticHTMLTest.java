package com.jimplush.goose;

import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jim Plush
 * User: jim
 * Date: 5/13/11
 */
public class StaticHTMLTest extends TestCase {

  public void testScribd() {

    String html = getHTML("/com/jimplush/goose/statichtmlassets/scribd.txt");
    String url = "http://www.scribd.com/doc/52584146/Microfinance-and-Poverty-Reduction?in_collection=2987942";
    Configuration config = new Configuration();
    config.setEnableImageFetching(false);
//    ContentExtractor contentExtractor = new ContentExtractor(config);
//    Article article = contentExtractor.extractContent(url, html);
//    assertTrue(article.getCleanedArticleText().startsWith("Microfinance and Poverty Reduction Susan Johnson and Ben Rogaly"));

  }


  private String getHTML(String filename) {
    String html = "";
    InputStream is = getClass().getResourceAsStream(filename);
    try {
      html = IOUtils.toString(is, "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return html;
  }

}
