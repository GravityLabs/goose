package com.jimplush.goose;

import junit.framework.TestCase;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * User: jim
 * Date: 12/16/10
 */


public class ContentExtractorTest extends TestCase {
  private static final Logger logger = Logger.getLogger(ContentExtractorTest.class);


  public void testArticleExtraction() {

  }


  public void testBadURLsThrowException() {
    ArrayList emptyList = new ArrayList();
    int x = 0;

    try {
    ContentExtractor contentExtractor = new ContentExtractor();
       Article article = contentExtractor.extractContent("htp://bob.c");
    } catch (IllegalArgumentException e) {
       x=1;
    }

    // asset the exception object
    assertEquals(1,x);



  }

}
