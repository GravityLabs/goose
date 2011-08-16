package com.jimplush.goose; /**
 * User: jim
 * Date: 12/18/10
 */

import com.jimplush.goose.extractors.AdditionalDataExtractor;
import com.jimplush.goose.extractors.PublishDateExtractor;
import com.jimplush.goose.images.Image;
import junit.framework.TestCase;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class GoldSitesTestIT extends TestCase {
  private static final Configuration DEFAULT_CONFIG = new Configuration();
  private static final Configuration NO_IMAGE_CONFIG = new Configuration();

  static {
    NO_IMAGE_CONFIG.setEnableImageFetching(false);
  }

  public void testHuffingtonPost() {
    String url = "http://www.huffingtonpost.com/2010/08/13/federal-reserve-pursuing_n_681540.html";
    Article article = getArticle(url);
    String expectedTitle = "Federal Reserve's Low Rate Policy Is A 'Dangerous Gamble,' Says Top Central Bank Official";
    String expectedStart = "A top regional Federal Reserve official sharply";
    String expectedKeywords = "federal, reserve's, low, rate, policy, is, a, 'dangerous, gamble,', says, top, central, bank, official, business";
    String expectedDescription = "A top regional Federal Reserve official sharply criticized Friday the Fed's ongoing policy of keeping interest rates near zero -- and at record lows -- as a \"dangerous gamble.\"";

    runArticleAssertions(article, expectedTitle, expectedStart, null, expectedDescription, expectedKeywords);

    // expected tags:
    Set<String> expectedTags = new HashSet<String>(16);
    expectedTags.add("Federal Open Market Committee");
    expectedTags.add("Federal Reserve");
    expectedTags.add("Federal Reserve Bank Of Kansas City");
    expectedTags.add("Financial Crisis");
    expectedTags.add("Financial Reform");
    expectedTags.add("Financial Regulation");
    expectedTags.add("Financial Regulatory Reform");
    expectedTags.add("Fomc");
    expectedTags.add("Great Recession");
    expectedTags.add("Interest Rates");
    expectedTags.add("Kansas City Fed");
    expectedTags.add("Monetary Policy");
    expectedTags.add("The Financial Fix");
    expectedTags.add("Thomas Hoenig");
    expectedTags.add("Too Big To Fail");
    expectedTags.add("Wall Street Reform");
    expectedTags.add("Business News");

    assertNotNull("Tags should not be NULL!", article.getTags());
    assertTrue("Tags should not be empty!", article.getTags().size() > 0);

    for (String actualTag : article.getTags()) {
      assertTrue("Each Tag should be contained in the expected set!", expectedTags.contains(actualTag));
    }
  }

  public void testTechCrunch() {
    String url = "http://techcrunch.com/2010/08/13/gantto-takes-on-microsoft-project-with-web-based-project-management-application/";
    Article article = getArticle(url);
    String expectedTitle = "Gantto Takes On Microsoft Project With Web-Based Project Management Application";
    String expectedStart = "Y Combinator-backed Gantto is launching";
    String expectedImage = "http://tctechcrunch.files.wordpress.com/2010/08/tour.jpg?w=640";

    runArticleAssertions(article, expectedTitle, expectedStart, expectedImage);

  }

  public void testCNN() {
    String url = "http://www.cnn.com/2010/POLITICS/08/13/democrats.social.security/index.html";
    Article article = getArticle(url);

    runArticleAssertions(article, "Democrats to use Social Security against GOP this fall", "Washington (CNN) -- Democrats pledged ", "http://i.cdn.turner.com/cnn/2010/POLITICS/08/13/democrats.social.security/story.kaine.gi.jpg");
  }

  public void testBusinessWeek() {

    String url = "http://www.businessweek.com/magazine/content/10_34/b4192066630779.htm";
    Article article = getArticle(url);

    runArticleAssertions(article, "Olivia Munn: Queen of the Uncool", "Six years ago, Olivia Munn arrived in Hollywood with fading ambitions of making it as a sports reporter and set about deploying", "http://images.businessweek.com/mz/10/34/370/1034_mz_66popmunnessa.jpg");

  }

  public void testBusinessWeek2() {

    String url = "http://www.businessweek.com/magazine/content/10_34/b4192048613870.htm";
    Article article = getArticle(url);

    runArticleAssertions(article, "There's discord on Wall Street: Strategists at major American investment banks see a", "http://images.businessweek.com/mz/covers/current_120x160.jpg");
  }

  public void testFoxNews() {

    String url = "http://www.foxnews.com/politics/2010/08/14/russias-nuclear-help-iran-stirs-questions-improved-relations/";
    Article article = getArticle(url);

    runArticleAssertions(article, "Russia's announcement that it will help Iran get nuclear fuel is raising questions", "http://a57.foxnews.com/static/managed/img/Politics/396/223/startsign.jpg");
  }

  public void testAOLNews() {

    String url = "http://www.aolnews.com/nation/article/the-few-the-proud-the-marines-getting-a-makeover/19592478";
    Article article = getArticle(url);

    runArticleAssertions(article, "WASHINGTON (Aug. 13) -- Declaring \"the maritime soul of the Marine Corps\" is", "http://o.aolcdn.com/photo-hub/news_gallery/6/8/680919/1281734929876.JPEG");
  }

  public void testWallStreetJournal() {

    String url = "http://online.wsj.com/article/SB10001424052748704532204575397061414483040.html";
    Article article = getArticle(url);

    runArticleAssertions(article, "The Obama administration has paid out less than a third of the nearly $230 billion", "http://si.wsj.net/public/resources/images/OB-JO747_stimul_G_20100814113803.jpg");
  }

  public void testUSAToday() {

    String url = "http://content.usatoday.com/communities/thehuddle/post/2010/08/brett-favre-practices-set-to-speak-about-return-to-minnesota-vikings/1";
    Article article = getArticle(url);

    runArticleAssertions(article, "Brett Favre couldn't get away from the", "http://i.usatoday.net/communitymanager/_photos/the-huddle/2010/08/18/favrespeaksx-inset-community.jpg");
  }

  public void testUSAToday2() {

    String url = "http://content.usatoday.com/communities/driveon/post/2010/08/gm-finally-files-for-ipo/1";
    Article article = getArticle(url);

    runArticleAssertions(article, "General Motors just filed with the Securities and Exchange ", "http://i.usatoday.net/communitymanager/_photos/drive-on/2010/08/18/cruzex-wide-community.jpg");
  }

  public void testESPN() {

    String url = "http://sports.espn.go.com/espn/commentary/news/story?id=5461430";
    Article article = getArticle(url);

    runArticleAssertions(article, "If you believe what college football coaches have said about sports", "http://a.espncdn.com/photo/2010/0813/ncf_i_mpouncey1_300.jpg");
  }

  public void testESPN2() {

    String url = "http://sports.espn.go.com/golf/pgachampionship10/news/story?id=5463456";
    Article article = getArticle(url);

    runArticleAssertions(article, "SHEBOYGAN, Wis. -- The only number that matters at the PGA Championship", "http://a.espncdn.com/media/motion/2010/0813/dm_100814_pga_rinaldi.jpg");
  }

  public void testWashingtonPost() {

    String url = "http://www.washingtonpost.com/wp-dyn/content/article/2010/12/08/AR2010120803185.html";
    Article article = getArticle(url);

    runArticleAssertions(article, "The Supreme Court sounded ", "http://media3.washingtonpost.com/wp-dyn/content/photo/2010/10/09/PH2010100904575.jpg");
  }

  public void testGizmodo() {

    String url = "http://gizmodo.com/#!5616256/xbox-kinect-gets-its-fight-club";
    Article article = getArticle(url);

    runArticleAssertions(article, "You love to punch your arms through the air", "http://cache.gawkerassets.com/assets/images/9/2010/08/500x_fighters_uncaged__screenshot_3b__jawbreaker.jpg");
  }

  public void testEngadget() {

    String url = "http://www.engadget.com/2010/08/18/verizon-fios-set-top-boxes-getting-a-new-hd-guide-external-stor/";
    Article article = getArticle(url);

    runArticleAssertions(article, "Streaming and downloading TV content to mobiles is nice", "http://www.blogcdn.com/www.engadget.com/media/2010/08/44ni600.jpg");
  }

  public void testBoingBoing() {

    String url = "http://www.boingboing.net/2010/08/18/dr-laura-criticism-o.html";
    Article article = getArticle(url);

    runArticleAssertions(article, "Dr. Laura Schlessinger is leaving radio to regain", "http://www.boingboing.net/images/drlaura.jpg");
  }

  public void testWired() {

    String url = "http://www.wired.com/playbook/2010/08/stress-hormones-boxing/";

    // example of a custom PublishDateExtractor
    final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    Configuration config = new Configuration();
    config.setPublishDateExtractor(new PublishDateExtractor() {
      @Override
      public Date extract(Element rootElement) {
        // look for this guy: <meta name="DisplayDate" content="2010-08-18" />
        Elements elements = Selector.select("meta[name=DisplayDate]", rootElement);
        if (elements.size() == 0) return null;
        Element metaDisplayDate = elements.get(0);
        if (metaDisplayDate.hasAttr("content")) {
          String dateStr = metaDisplayDate.attr("content");
          try {
            return fmt.parse(dateStr);
          } catch (ParseException e) {
            return null;
          }
        }

        return null;
      }
    });

    Article article = getArticle(url, config);

    runArticleAssertions(
        article,
        "Stress Hormones Could Predict Boxing Dominance", "On November 25, 1980, professional boxing",
        "http://www.wired.com/playbook/wp-content/uploads/2010/08/fight_f-660x441.jpg");

    String expectedDateString = "2010-08-18";
    assertNotNull("publishDate should not be null!", article.getPublishDate());
    assertEquals("Publish date should equal: \"2010-08-18\"", expectedDateString, fmt.format(article.getPublishDate()));
    System.out.println("Publish Date Extracted: " + fmt.format(article.getPublishDate()));
  }

  public void testGigaOhm() {

    String url = "http://gigaom.com/apple/apples-next-macbook-an-800-mac-for-the-masses/";
    Article article = getArticle(url);

    runArticleAssertions(article, "The MacBook Air is a bold move forward ", "http://gigapple.files.wordpress.com/2010/10/macbook-feature.png?w=300&h=200");
  }

  public void testMashable() {

    String url = "http://mashable.com/2010/08/18/how-tonot-to-ask-someone-out-online/";

    Configuration config = new Configuration();
    config.setAdditionalDataExtractor(new AdditionalDataExtractor() {
      @Override
      public Map<String, String> extract(Element rootElement) {
        Elements elements = Selector.select("span.author > a", rootElement);
        if (elements.size() == 0) return null;
        String author = elements.get(0).text();
        Map<String, String> result = new HashMap<String, String>(1);
        result.put("author", author);
        return result;
      }
    });
    Article article = getArticle(url, config);

    runArticleAssertions(article, "Imagine, if you will, a crowded dance floor", "http://9.mshcdn.com/wp-content/uploads/2010/07/love.jpg");

    assertNotNull("additionalData should not be null!", article.getAdditionalData());
    assertEquals("additionalData.get(\"author\") should be as expected!", "Brenna Ehrlich", article.getAdditionalData().get("author"));
  }

  public void testReadWriteWeb() {

    String url = "http://www.readwriteweb.com/start/2010/08/pagely-headline.php";
    Article article = getArticle(url);

    runArticleAssertions(article, "In the heart of downtown Chandler, Arizona", "http://rww.readwriteweb.netdna-cdn.com/start/images/pagelyscreen_aug10.jpg");
  }

  public void testVentureBeat() {

    String url = "http://social.venturebeat.com/2010/08/18/facebook-reveals-the-details-behind-places/";
    Article article = getArticle(url);

    runArticleAssertions(article, "Facebook just confirmed the rumors", "http://venturebeat.files.wordpress.com/2010/08/mark-zuckerberg-facebook-places.jpg?w=300&h=219");
  }

  public void testTimeMagazine() {

    String url = "http://www.time.com/time/health/article/0,8599,2011497,00.html";
    Article article = getArticle(url);

    runArticleAssertions(article, "This month, the federal government released", "http://img.timeinc.net/time/daily/2010/1008/bp_oil_spill_0817.jpg");
  }

  public void testCnet() {

    String url = "http://news.cnet.com/8301-30686_3-20014053-266.html?tag=topStories1";
    Article article = getArticle(url);

    runArticleAssertions(article, "NEW YORK--Verizon Communications is prepping a new", "http://i.i.com.com/cnwk.1d/i/tim//2010/08/18/Verizon_iPad_and_live_TV_610x458.JPG");
  }

  public void testYahooNews() {

    String url = "http://news.yahoo.com/midwest-obama-seeks-own-economic-help-174721833.html";
    Article article = getArticle(url);

    runArticleAssertions(article, "PEOSTA, Iowa (AP) — Seeking some help from rural America, President Barack Obama on Tuesday", "http://l.yimg.com/bt/api/res/1.2/zyndHmia7sZSWsxYJsjlYA--/YXBwaWQ9eW5ld3M7Zmk9ZmlsbDtoPTkwO3E9ODU7dz05MA--/http://media.zenfs.com/en_us/News/ap_webfeeds/2aed17a65c710912f60e6a706700abb4.jpg.cf.png");
  }

  public void testPolitico() {

    String url = "http://www.politico.com/news/stories/1010/43352.html";
    Article article = getArticle(url);

    runArticleAssertions(article, "If the newest Census Bureau estimates stay close to form", "http://images.politico.com/global/news/100927_obama22_ap_328.jpg");
  }

  public void testNewsweek() {

    String url = "http://www.newsweek.com/2010/10/09/how-moscow-s-war-on-islamist-rebels-is-backfiring.html";
    Article article = getArticle(url);

    runArticleAssertions(article, "The video shows a gun barrel jutting from the rear", "http://www.thedailybeast.com/newsweek/2010/10/09/how-moscow-s-war-on-islamist-rebels-is-backfiring.img.200.jpg");
  }

  public void testLifeHacker() {

    String url = "http://lifehacker.com/#!5659837/build-a-rocket-stove-to-heat-your-home-with-wood-scraps";
    Article article = getArticle(url);

    runArticleAssertions(article, "If you find yourself with lots of leftover wood", "http://cache.gawker.com/assets/images/lifehacker/2010/10/rocket-stove-finished.jpeg");
  }

  public void testNinjaBlog() {

    String url = "http://www.ninjatraderblog.com/im/2010/10/seo-marketing-facts-about-google-instant-and-ranking-your-website/";
    Article article = getArticle(url, false);

    runArticleAssertions(article, "Many users around the world Google their queries");
  }

  public void testNaturalHomeBlog() {

    String url = "http://www.naturalhomeandgarden.com/remodeling/small-steps-to-a-green-remodel.aspx";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "Though green remodeling sometimes involves big-ticket items like solar panels",
        "http://www.naturalhomeandgarden.com/uploadedImages/articles/issues/2011-05-01/NH-MA11-front-home_resized400X266.jpg");
  }

  public void testSFGate() {

    String url = "http://www.sfgate.com/cgi-bin/article.cgi?f=/c/a/2010/10/27/BUD61G2DBL.DTL";
    Article article = getArticle(url);

    runArticleAssertions(article, "Fewer homes in California and", "http://imgs.sfgate.com/c/pictures/2010/10/26/ba-foreclosures2_SFCG1288130091.jpg");
  }

  public void testSportsIllustrated() {

    String url = "http://sportsillustrated.cnn.com/2011/writers/peter_king/05/08/mmqb/index.html?sct=nfl_t11_a0";
    Article article = getArticle(url);

    runArticleAssertions(article, "Many of you have written or tweeted to ask whether I'll be covering more", "http://i2.cdn.turner.com/si/2011/writers/peter_king/05/08/mmqb/jake-locker.jpg");
  }

  public void testStarMagazine() {

    String url = "http://www.starmagazine.com/news/17510?cid=RSS";
    Article article = getArticle(url);

    runArticleAssertions(article, "The Real Reason Rihanna Skipped Katy's Wedding: No Cell Phone Reception!", "Rihanna has admitted the real reason she was a no show at her", "http://www.starmagazine.com/sites/starmagazine.com/files/imagecache/node_page_image/article_images/Rihanna_1010_230.jpg");

  }

  public void testDailyBeast() {

    String url = "http://www.thedailybeast.com/blogs-and-stories/2010-11-01/ted-sorensen-speechwriter-behind-jfks-best-jokes/?cid=topic:featured1";
    Article article = getArticle(url);

    runArticleAssertions(article, "Today, as wordsmiths, politicos and Kennedyophiles alike", "http://www.thedailybeast.com/content/dailybeast/articles/2010/11/01/ted-sorensen-speechwriter-behind-jfks-best-jokes/_jcr_content/body/image_0.img.jpg/1302761778490.jpg");
  }

  public void testBloomberg() {

    String url = "http://www.bloomberg.com/news/2010-11-01/china-becomes-boss-in-peru-on-50-billion-mountain-bought-for-810-million.html";
    Article article = getArticle(url);

    runArticleAssertions(article, "The Chinese entrepreneur and the Peruvian shopkeeper", "http://www.bloomberg.com/apps/data?pid=avimage&iid=iimODmqjtcQU");
  }

  public void testScientificDaily() {

    String url = "http://www.scientificamerican.com/article.cfm?id=bpa-semen-quality";
    Article article = getArticle(url);

    runArticleAssertions(article, "Everyday BPA Exposure Decreases Human Semen Quality", "The common industrial chemical bisphenol A (BPA) ", "http://www.scientificamerican.com/media/inline/bpa-semen-quality_1.jpg");
  }

  public void testSlamMagazine() {

    String url = "http://www.slamonline.com/online/nba/2010/10/nba-schoolyard-rankings/";
    Article article = getArticle(url);

    runArticleAssertions(article, "NBA Schoolyard Rankings", "There are literally dozens of ways to predetermine", "http://www.slamonline.com/online/wp-content/uploads/2010/10/celtics.jpg");
  }

  public void testTheFrisky() {

    String url = "http://www.thefrisky.com/post/246-rachel-dratch-met-her-baby-daddy-in-a-bar/?eref=RSS";
    Article article = getArticle(url);

    runArticleAssertions(article, "Rachel Dratch Met Her Baby Daddy At A Bar", "Rachel Dratch had been keeping the identity of her baby daddy ", "http://cdn.thefrisky.com/images/uploads/rachel_dratch_102810_m.jpg");
  }

  public void testUniverseToday() {

    String url = "http://www.universetoday.com/76881/podcast-more-from-tony-colaprete-on-lcross/";
    Article article = getArticle(url);

    runArticleAssertions(article, "More From Tony Colaprete on LCROSS", "I had the chance to interview LCROSS", "http://www.universetoday.com/wp-content/uploads/2009/10/lcross-impact_01_01.jpg");

  }


  public void testCNBC() {

    String url = "http://www.cnbc.com/id/40491584";
    Article article = getArticle(url);

    runArticleAssertions(article, "Chinese Art Expert 'Skeptical' of Record-Setting Vase", "A prominent expert on Chinese works ", "http://media.cnbc.com/i/CNBC/Sections/News_And_Analysis/__Story_Inserts/graphics/__ART/chinese_vase_150.jpg");

  }

  public void testEspnWithFlashVideo() {

    String url = "http://sports.espn.go.com/nfl/news/story?id=5971053";
    Article article = getArticle(url);

    runArticleAssertions(article, "Michael Vick of Philadelphia Eagles misses practice, unlikely to play vs. Dallas Cowboys", "PHILADELPHIA -- Michael Vick missed practice Thursday", "http://a.espncdn.com/i/espn/espn_logos/espn_red.png");

  }

  public void testSportingNews() {

    String url = "http://www.sportingnews.com/nfl/feed/2011-01/nfl-coaches/story/raiders-cut-ties-with-cable";
    Article article = getArticle(url);

    runArticleAssertions(article, "Raiders cut ties with Cable", "ALAMEDA, Calif. — The Oakland Raiders informed coach Tom Cable", "http://dy.snimg.com/story-image/0/69/174475/14072-650-366.jpg");

  }

  public void testFoxSports() {

    String url = "http://msn.foxsports.com/nfl/story/Tom-Cable-fired-contract-option-Oakland-Raiders-coach-010411";
    Article article = getArticle(url, false);

    runArticleAssertions(
        article,
        "Oakland Raiders won't bring Tom Cable back as coach - NFL News",
        "The Oakland Raiders informed coach Tom Cable",
        null);
  }

  public void testMSNBC() {

    String url = "http://www.msnbc.msn.com/id/43085992/ns/business-stocks_and_economy/";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "LinkedIn share price soars in market debut",
        "There was an unmistakable echo of the dot-com boom Thursday",
        "http://msnbcmedia1.msn.com/j/MSNBC/Components/Photo/_new/110519_linkedin_hmed_0755p.grid-6x2.jpg");

  }

  public void testEconomist() {

    String url = "http://www.economist.com/node/18802932";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "READERS who were paying attention in their maths classes may recall",
        "http://media.economist.com/images/images-magazine/2011/06/11/st/20110611_stp003.jpg");

  }


  public void testTheAtlantic() {

    String url = "http://www.theatlantic.com/culture/archive/2011/01/how-to-stop-james-bond-from-getting-old/69695/";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "If James Bond could age, he'd be well into his 90s right now",
        "http://cdn.theatlantic.com/static/mt/assets/culture_test/James%20Bond_post.jpg");
  }

  public void testGawker() {

    String url = "http://gawker.com/#!5777023/charlie-sheen-is-going-to-haiti-with-sean-penn";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "With a backlash brewing against the incessant media",
        "http://cache.gawkerassets.com/assets/images/7/2011/03/medium_0304_pennsheen.jpg");
  }

  public void testNyTimes() {

    String url = "http://www.nytimes.com/2011/06/13/business/media/13link.html";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "The potential presidential candidate Sarah Palin has been criticized",
        "http://graphics8.nytimes.com/images/2011/06/13/business/link1/link1-articleInline.jpg");
  }

  public void testTheVacationGals() {

    String url = "http://thevacationgals.com/vacation-rental-homes-are-a-family-reunion-necessity/";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "Editors’ Note: We are huge proponents",
        "http://thevacationgals.com/wp-content/uploads/2010/11/Gemmel-Family-Reunion-at-a-Vacation-Rental-Home1-300x225.jpg");
  }


  public void testShockYa() {

    String url = "http://www.shockya.com/news/2011/01/30/daily-shock-jonathan-knight-of-new-kids-on-the-block-publicly-reveals-hes-gay/";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "New Kids On The Block singer Jonathan Knight has publicly",
        "http://www.shockya.com/news/wp-content/uploads/jonathan_knight_new_kids_gay.jpg");
  }


  public void testLiveStrong() {

    String url = "http://www.livestrong.com/article/395538-how-to-decrease-the-rates-of-obesity-in-children/";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "Childhood obesity increases a young person",
        "http://photos.demandstudios.com/getty/article/184/46/87576279_XS.jpg");
  }

  public void testLiveStrong2() {
    String url = "http://www.livestrong.com/article/396152-do-resistance-bands-work-for-strength-training/";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "Resistance bands or tubes are named because",
        "http://photos.demandstudios.com/getty/article/142/66/86504893_XS.jpg");
  }

  public void testCracked() {
    String url = "http://www.cracked.com/article_19029_6-things-social-networking-sites-need-to-stop-doing.html";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "Social networking is here to stay",
        "http://i-beta.crackedcdn.com/phpimages/article/2/1/5/45215.jpg?v=1");
  }

  public void testTrailsCom() {
    String url = "http://www.trails.com/facts_41596_hot-spots-citrus-county-florida.html";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "Snorkel and view artificial reefs or chase",
        "http://cdn-www.trails.com/imagecache/articles/295x195/hot-spots-citrus-county-florida-295x195.png");
  }

  public void testTrailsCom2() {
    String url = "http://www.trails.com/facts_12408_history-alpine-skis.html";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "Derived from the old Norse word",
        "http://cdn-www.trails.com/imagecache/articles/295x195/history-alpine-skis-295x195.png");
  }

  public void testEhow() {
    String url = "http://www.ehow.com/how_7734109_make-white-spaghetti.html";
    Article article = getArticle(url, false);

    runArticleAssertions(
        article,
        "How to Make White Spaghetti",
        "For a filling and tasty dinner that doesn't require",
        null);
  }


  public void testGolfLink() {
    String url = "http://www.golflink.com/how_1496_eat-cheap-las-vegas.html";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "Las Vegas, while noted for its glitz",
        "http://cdn-www.golflink.com/Cms/images/GlobalPhoto/Articles/2011/2/17/1496/fotolia4152707XS-main_Full.jpg");
  }

  public void testTimeMagazine2() {
    String url = "http://www.time.com/time/specials/packages/article/0,28804,2065531_2065534,00.html";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "The traditional Passover song",
        "http://img.timeinc.net/time/photoessays/2011/top10_passover/scallions.jpg");
  }

  public void testWSJ2() {
    String url = "http://online.wsj.com/article/SB10001424052748704004004576270433180029082.html?mod=WSJ_hp_LEFTTopStories";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "Stocks recorded their biggest decline in more than a month",
        "http://m.wsj.net/video/20110418/041811hubpmmarkets2/041811hubpmmarkets2_512x288.jpg");
  }

  public void testBuzznet() {
    String url = "http://wevegotyoucovered.buzznet.com/user/journal/8048821/buzznet-talks-bamboozle-festival-founder/";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "Bamboozle approaches! The three day music megafest",
        "http://cdn.buzznet.com/assets/imgx/1/4/0/6/1/1/9/1/orig-14061191.jpg");
  }


  public void testTheSuperFicial() {
    String url = "http://www.thesuperficial.com/teen-mom-leah-divorce-corey-04-2011";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "Christ, did they all get implants?",
        "http://cdn03.cdn.thesuperficial.com/wp-content/uploads/2011/04/0418-teen-mom-leah-messer-divorce-14-480x720.jpg");
  }

  public void testScribd() {
    String url = "http://www.scribd.com/doc/49951733/CNBC-Warren-Buffett-Transcript-March-2-2011";
    Article article = getArticle(url, false);

    runArticleAssertions(
        article,
        "This is a transcript of his");
  }

  public void testScribd2() {
    String url = "http://www.scribd.com/doc/23433951/10-Trends-to-Watch-2010";
    Article article = getArticle(url, false);

    runArticleAssertions(
        article,
        "t is hard to believe another year has come");
  }

  public void testDogster() {
    String url = "http://blogs.dogster.com/dog-training/dogs-understand-intimacy/2011/05/";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "Children and the elderly are the two populations",
        "http://www.theotherendoftheleash.com/theotherendoftheleash/uploads/2010/02/monkey-hugs-dog.jpg");
  }

  // todo we're mssing the first actual paragraph
  public void testCatster() {
    String url = "http://blogs.catster.com/kitty-news-network/2011/05/03/survey-cat-owners-prefer-the-adoption-option/";
    Article article = getArticle(url);

    runArticleAssertions(
        article,
        "There’s good news in the air for shelter cats",
        "http://b1.cdnsters.com/kitty-news-network/files/2011/05/thomas-and-dahlia-300x225.jpg");
  }

  public void testScribd3() {
    String url = "http://www.scribd.com/doc/21795768/1-What-is-NLS-NLS-Stands-for-National-Language";
    Article article = getArticle(url, false);

    runArticleAssertions(
        article,
        "Oracle Applications, it basically means the ability to run");
  }

  public void testScribd4() {
    String url = "http://www.scribd.com/doc/52584146/Microfinance-and-Poverty-Reduction?in_collection=2987942";
    Article article = getArticle(url, false);
    runArticleAssertions(article, "Microfinance and Poverty Reduction Susan Johnson and Ben Rogaly");
  }

  private Article getArticle(String url) {
    return getArticle(url, true);
  }

  private Article getArticle(String url, boolean fetchImages) {
    return getArticle(url, fetchImages ? DEFAULT_CONFIG : NO_IMAGE_CONFIG);
  }

  private Article getArticle(String url, Configuration config) {
    ContentExtractor extractor = new ContentExtractor(config);
    return extractor.extractContent(url);
  }

  private void runArticleAssertions(Article article, String expectedStart) {
    runArticleAssertions(article, null, expectedStart, null, null, null);
  }

  private void runArticleAssertions(Article article, String expectedStart, String expectedImage) {
    runArticleAssertions(article, null, expectedStart, expectedImage, null, null);
  }

  private void runArticleAssertions(Article article, String expectedTitle, String expectedStart, String expectedImage) {
    runArticleAssertions(article, expectedTitle, expectedStart, expectedImage, null, null);
  }

  private static final char NL = '\n';
  private static final char TAB = '\t';
  private static StringBuilder tagReport = new StringBuilder("=======================::. TAG REPORT .::======================\n");

  private void runArticleAssertions(Article article, String expectedTitle, String expectedStart, String expectedImage, String expectedDescription, String expectedKeywords) {
    assertNotNull("Resulting article was NULL!", article);

    if (article.getTags().size() > 0) {
      tagReport.append("BEGIN URL:").append(TAB).append(article.getCanonicalLink()).append(NL).append(TAB);
      tagReport.append("Extracted: ").append(article.getTags().size()).append(" TAGs").append(NL);
      int i = 0;
      for (String tag : article.getTags()) {
        tagReport.append(TAB).append(TAB).append("# ").append(++i).append(": ").append(tag).append(NL);
      }
      tagReport.append("END URL:").append(TAB).append(article.getCanonicalLink()).append(NL);
    }

    if (expectedTitle != null) {
      String title = article.getTitle();
      assertNotNull("Title was NULL!", title);
      assertEquals("Expected title was not returned!", expectedTitle, title);
    }

    if (expectedStart != null) {
      String articleText = article.getCleanedArticleText();
      assertNotNull("Resulting article text was NULL!", articleText);
      assertTrue("Article text was not as long as expected beginning!", expectedStart.length() <= articleText.length());
      String actual = articleText.substring(0, expectedStart.length());
      assertEquals("The beginning of the article text was not as expected!", expectedStart, actual);
    }

    if (expectedImage != null) {
      Image image = article.getTopImage();
      assertNotNull("Top image was NULL!", image);
      String src = image.getImageSrc();
      assertNotNull("Image src was NULL!", src);
      assertEquals("Image src was not as expected!", expectedImage, src);
    }

    if (expectedDescription != null) {
      String description = article.getMetaDescription();
      assertNotNull("Meta Description was NULL!", description);
      assertEquals("Meta Description was not as expected!", expectedDescription, description);
    }

    if (expectedKeywords != null) {
      String keywords = article.getMetaKeywords();
      assertNotNull("Meta Keywords was NULL!", keywords);
      assertEquals("Meta Keywords was not as expected!", expectedKeywords, keywords);
    }
  }

  protected static void printReport() {
    System.out.print(tagReport);
  }
}

