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
    assertTrue(article.getCleanedArticleText().startsWith("Y Combinator-backed Gantto is launching"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://tctechcrunch.files.wordpress.com/2010/08/tour.jpg"));

  }

  public void testCNN() {
    ContentExtractor contentExtractor = new ContentExtractor();
    String url = "http://www.cnn.com/2010/POLITICS/08/13/democrats.social.security/index.html";
    Article article = contentExtractor.extractContent(url);
    assertEquals("Democrats to use Social Security against GOP this fall", article.getTitle());
    assertTrue(article.getCleanedArticleText().startsWith("Washington (CNN) -- Democrats pledged "));
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

  public void testFoxNews() {

    String url = "http://www.foxnews.com/politics/2010/08/14/russias-nuclear-help-iran-stirs-questions-improved-relations/";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Russia's announcement that it will help Iran get nuclear fuel is raising questions"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://a57.foxnews.com/static/managed/img/Politics/397/224/startsign.jpg"));
  }

   public void testAOLNews() {

    String url = "http://www.aolnews.com/nation/article/the-few-the-proud-the-marines-getting-a-makeover/19592478";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("WASHINGTON (Aug. 13) -- Declaring &quot;the maritime soul of the Marine Corps"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://o.aolcdn.com/photo-hub/news_gallery/6/8/680919/1281734929876.JPEG"));
  }

  public void testWallStreetJournal() {

    String url = "http://online.wsj.com/article/SB10001424052748704532204575397061414483040.html";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("The Obama administration has paid out less than a third of the nearly $230 billion"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://si.wsj.net/public/resources/images/OB-JO747_stimul_G_20100814113803.jpg"));
  }

  public void testUSAToday() {

    String url = "http://content.usatoday.com/communities/thehuddle/post/2010/08/brett-favre-practices-set-to-speak-about-return-to-minnesota-vikings/1";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Brett Favre couldn't get away from the"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://i.usatoday.net/communitymanager/_photos/the-huddle/2010/08/18/favrespeaksx-inset-community.jpg"));
  }

  public void testUSAToday2() {

    String url = "http://content.usatoday.com/communities/driveon/post/2010/08/gm-finally-files-for-ipo/1";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("General Motors just filed with the Securities and Exchange "));
    assertTrue(article.getTopImage().getImageSrc().equals("http://i.usatoday.net/communitymanager/_photos/drive-on/2010/08/18/cruzex-wide-community.jpg"));
  }

  public void testESPN() {

    String url = "http://sports.espn.go.com/espn/commentary/news/story?id=5461430";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("If you believe what college football coaches have said about sports"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://a.espncdn.com/photo/2010/0813/ncf_i_mpouncey1_300.jpg"));
  }

  public void testESPN2() {

    String url = "http://sports.espn.go.com/golf/pgachampionship10/news/story?id=5463456";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("SHEBOYGAN, Wis. -- The only number that matters at the PGA Championship"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://a.espncdn.com/media/motion/2010/0813/dm_100814_pga_rinaldi.jpg"));
  }

  public void testWashingtonPost() {

    String url = "http://www.washingtonpost.com/wp-dyn/content/article/2010/12/08/AR2010120803185.html";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("The Supreme Court sounded "));
    assertTrue(article.getTopImage().getImageSrc().equals("http://media3.washingtonpost.com/wp-dyn/content/photo/2010/10/09/PH2010100904575.jpg"));
  }

  public void testGizmodo() {

    String url = "http://gizmodo.com/5616256/xbox-kinect-gets-its-fight-club";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("You love to punch your arms through the air"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://cache.gawkerassets.com/assets/images/9/2010/08/500x_fighters_uncaged__screenshot_3b__jawbreaker.jpg"));
  }

  public void testEngadget() {

    String url = "http://www.engadget.com/2010/08/18/verizon-fios-set-top-boxes-getting-a-new-hd-guide-external-stor/";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Streaming and downloading TV content to mobiles is nice"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://www.blogcdn.com/www.engadget.com/media/2010/08/44ni600.jpg"));
  }


}
