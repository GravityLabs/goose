package com.jimplush.goose;

import junit.framework.TestCase;
import org.apache.log4j.Logger;

/**
 * User: jim
 * Date: 12/16/10
 */

public class ArticleTest extends TestCase {
  private static final Logger logger = Logger.getLogger(ArticleTest.class);

  public void testArticle()
  {
    Article article = new Article();
    article.setTitle("This is a title");
    assertEquals("This is a title", article.getTitle());
  }


  public void testSettingDomainOnArticle() {

    Article article = new Article();
    article.setDomain("http://grapevinyl.com/v/84/magnetic-morning/getting-nowhere");
    assertEquals("grapevinyl.com", article.getDomain());

    article.setDomain("http://www.economist.com/v/84/magnetic-morning/getting-nowhere");
    assertEquals("www.economist.com", article.getDomain());
  }


}
