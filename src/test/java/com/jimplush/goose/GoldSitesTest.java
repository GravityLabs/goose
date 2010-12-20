package com.jimplush.goose; /**
 * User: jim
 * Date: 12/18/10
 */

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class GoldSitesTest extends TestCase {
  private static final Logger logger = Logger.getLogger(GoldSitesTest.class);

  public void testHuffingtonPost() {
    ContentExtractor contentExtractor = new ContentExtractor();
    String url = "http://www.huffingtonpost.com/2010/08/13/federal-reserve-pursuing_n_681540.html";
    Article article = contentExtractor.extractContent(url);
    assertEquals("Federal Reserve's Low Rate Policy Is A 'Dangerous Gamble,' Says Top Central Bank Official", article.getTitle());
    assertEquals("federal, reserve's, low, rate, policy, is, a, 'dangerous, gamble,', says, top, central, bank, official, business", article.getMetaKeywords());
    assertEquals("A top regional Federal Reserve official sharply criticized Friday the Fed's ongoing policy of keeping interest rates near zero -- and at record lows -- as a \"dangerous gamble.\"", article.getMetaDescription());
    assertTrue(article.getCleanedArticleText().startsWith("A top regional Federal Reserve official sharply"));
  }

  public void testTechCrunch() {
    ContentExtractor contentExtractor = new ContentExtractor();
    String url = "http://techcrunch.com/2010/08/13/gantto-takes-on-microsoft-project-with-web-based-project-management-application/";
    Article article = contentExtractor.extractContent(url);
    assertEquals("Gantto Takes On Microsoft Project With Web-Based Project Management Application", article.getTitle());
    assertTrue(article.getTopNode().text().startsWith("Y Combinator-backed Gantto is launching"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://tctechcrunch.files.wordpress.com/2010/08/tour.jpg"));

  }

  public void testCNN() {
    ContentExtractor contentExtractor = new ContentExtractor();
    String url = "http://www.cnn.com/2010/POLITICS/08/13/democrats.social.security/index.html";
    Article article = contentExtractor.extractContent(url);
    assertEquals("Democrats to use Social Security against GOP this fall", article.getTitle());
    assertTrue(article.getTopNode().text().startsWith("Washington (CNN) -- Democrats pledged "));
    assertTrue(article.getTopImage().getImageSrc().equals("http://i.cdn.turner.com/cnn/2010/POLITICS/08/13/democrats.social.security/story.kaine.gi.jpg"));
  }

  public void testBusinessWeek() {

    String url = "http://www.businessweek.com/magazine/content/10_34/b4192066630779.htm";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertEquals("Olivia Munn: Queen of the Uncool", article.getTitle());
    assertTrue(article.getCleanedArticleText().startsWith("Six years ago, Olivia Munn arrived in Hollywood with fading ambitions of making it as a sports reporter and set about deploying"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://images.businessweek.com/mz/10/34/370/1034_mz_66popmunnessa.jpg"));

  }


  public void testBusinessWeek2() {

    String url = "http://www.businessweek.com/magazine/content/10_34/b4192048613870.htm";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("There's discord on Wall Street: Strategists at major American investment banks see a"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://images.businessweek.com/mz/covers/current_120x160.jpg"));
  }



}
