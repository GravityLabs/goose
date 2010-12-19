package com.jimplush.goose; /**
 * User: jim
 * Date: 12/18/10
 */

import junit.framework.TestCase;
import org.apache.log4j.Logger;


public class GoldSitesTest extends TestCase{
  private static final Logger logger = Logger.getLogger(GoldSitesTest.class);

  public void testHuffingtonPost()
  {

    ContentExtractor contentExtractor = new ContentExtractor();

    // article will now contain a nice little object full of the goods we extracted
    Article article = contentExtractor.extractContent("http://www.huffingtonpost.com/2010/08/13/federal-reserve-pursuing_n_681540.html");

    assertEquals("Federal Reserve's Low Rate Policy Is A 'Dangerous Gamble,' Says Top Central Bank Official",
            article.getTitle());

    assertEquals("federal, reserve's, low, rate, policy, is, a, 'dangerous, gamble,', says, top, central, bank, official, business",
            article.getMetaKeywords());

    assertEquals("A top regional Federal Reserve official sharply criticized Friday the Fed's ongoing policy of keeping interest rates near zero -- and at record lows -- as a \"dangerous gamble.\"",
            article.getMetaDescription());

    assertTrue(article.getTopNode().text().startsWith("A top regional Federal Reserve official sharply"));
  }


}
