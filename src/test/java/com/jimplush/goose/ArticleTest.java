package com.jimplush.goose;

import junit.framework.TestCase;


/**
 * User: jim
 * Date: 12/16/10
 */

public class ArticleTest extends TestCase {


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
