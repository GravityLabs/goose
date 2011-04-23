package com.jimplush.goose; /**
 * User: jim
 * Date: 12/18/10
 */

import junit.framework.TestCase;


public class GoldSitesTest extends TestCase {

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

    String url = "http://gizmodo.com/#!5616256/xbox-kinect-gets-its-fight-club";
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

  public void testBoingBoing() {

    String url = "http://www.boingboing.net/2010/08/18/dr-laura-criticism-o.html";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Dr. Laura Schlessinger is leaving radio to regain"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://www.boingboing.net/images/drlaura.jpg"));
  }

  public void testWired() {

    String url = "http://www.wired.com/playbook/2010/08/stress-hormones-boxing/";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("On November 25, 1980, professional boxing"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://www.wired.com/playbook/wp-content/uploads/2010/08/fight_f-660x441.jpg"));
    assertTrue(article.getTitle().equals("Stress Hormones Could Predict Boxing Dominance"));
  }

  public void tetGigaOhm() {

    String url = "http://gigaom.com/apple/apples-next-macbook-an-800-mac-for-the-masses/";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("The MacBook Air is a bold move forward "));
    assertTrue(article.getTopImage().getImageSrc().equals("http://gigapple.files.wordpress.com/2010/10/macbook-feature.png?w=300&h=200"));
  }

  public void testMashable() {

    String url = "http://mashable.com/2010/08/18/how-tonot-to-ask-someone-out-online/";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Imagine, if you will, a crowded dance floor"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://9.mshcdn.com/wp-content/uploads/2010/07/love.jpg"));
  }

  public void testReadWriteWeb() {

    String url = "http://www.readwriteweb.com/start/2010/08/pagely-headline.php";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("In the heart of downtown Chandler, Arizona"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://rww.readwriteweb.netdna-cdn.com/start/images/pagelyscreen_aug10.jpg"));
  }

  public void testVentureBeat() {

    String url = "http://social.venturebeat.com/2010/08/18/facebook-reveals-the-details-behind-places/";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Facebook just confirmed the rumors"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://cdn.venturebeat.com/wp-content/uploads/2010/08/mark-zuckerberg-facebook-places.jpg"));
  }

  public void testTimeMagazine() {

    String url = "http://www.time.com/time/health/article/0,8599,2011497,00.html";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("This month, the federal government released"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://img.timeinc.net/time/daily/2010/1008/bp_oil_spill_0817.jpg"));
  }

  public void testCnet() {

    String url = "http://news.cnet.com/8301-30686_3-20014053-266.html?tag=topStories1";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("NEW YORK--Verizon Communications is prepping a new"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://i.i.com.com/cnwk.1d/i/tim//2010/08/18/Verizon_iPad_and_live_TV_610x458.JPG"));
  }

  public void testYahooNewsEvenThoughTheyFuckedUpDeliciousWeWillTestThemAnyway() {

    String url = "http://news.yahoo.com/s/ap/20110305/ap_on_re_af/af_libya";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("TRIPOLI, Libya &ndash; Moammar Gadhafi"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://d.yimg.com/a/p/ap/20110305/capt.20433579e6a949189ddb65a8c260183c-20433579e6a949189ddb65a8c260183c-0.jpg?x=213&y=142&xc=1&yc=1&wc=410&hc=273&q=85&sig=i4WKbNKMgqenVsxU3NCbOg--"));
  }

  public void testPolitico() {

    String url = "http://www.politico.com/news/stories/1010/43352.html";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("If the newest Census Bureau estimates stay close to form"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://images.politico.com/global/news/100927_obama22_ap_328.jpg"));
  }

  public void testNewsweek() {

    String url = "http://www.newsweek.com/2010/10/09/how-moscow-s-war-on-islamist-rebels-is-backfiring.html";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("At first glance, Kadyrov might seem"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://www.newsweek.com/content/newsweek/2010/10/09/how-moscow-s-war-on-islamist-rebels-is-backfiring/_jcr_content/body/mainimage.img.jpg/1286654107500.jpg"));
  }

  public void testLifeHacker() {

    String url = "http://lifehacker.com/#!5659837/build-a-rocket-stove-to-heat-your-home-with-wood-scraps";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("If you find yourself with lots of leftover wood"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://cache.gawker.com/assets/images/lifehacker/2010/10/rocket-stove-finished.jpeg"));
  }

  public void testNinjaBlog() {

    String url = "http://www.ninjatraderblog.com/im/2010/10/seo-marketing-facts-about-google-instant-and-ranking-your-website/";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Many users around the world Google their queries"));
  }

  public void testNaturalHomeBlog() {

    String url = "http://www.naturalhomemagazine.com/diy-projects/try-this-papier-mache-ghostly-lanterns.aspx";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Guide trick or treaters and other friendly spirits to your front"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://www.naturalhomemagazine.com/uploadedImages/articles/issues/2010-09-01/NH-SO10-trythis-lantern-final2_resized400X266.jpg"));
  }

  public void testSFGate() {

    String url = "http://www.sfgate.com/cgi-bin/article.cgi?f=/c/a/2010/10/27/BUD61G2DBL.DTL";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Fewer homes in California and"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://imgs.sfgate.com/c/pictures/2010/10/26/ba-foreclosures2_SFCG1288130091.jpg"));
  }

  public void testSportsIllustrated() {

    String url = "http://sportsillustrated.cnn.com/2010/football/ncaa/10/15/ohio-state-holmes.ap/index.html?xid=si_ncaaf";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("COLUMBUS, Ohio (AP) -- Ohio State has closed"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://i.cdn.turner.com/si/.e1d/img/4.0/global/logos/si_100x100.jpg"));
  }

  // todo get this one working - I hate star magazine web designers, they put 2 html files into one
//  public void testStarMagazine() {
//
//    String url = "http://www.starmagazine.com/news/17510?cid=RSS";
//    ContentExtractor contentExtractor = new ContentExtractor();
//    Article article = contentExtractor.extractContent(url);
//    assertTrue(article.getCleanedArticleText().startsWith("The Real Reason Rihanna Skipped Katy's Wedding: No Cell Phone Reception!"));
//    assertTrue(article.getTopImage().getImageSrc().equals("Rihanna has admitted the real reason she was a no show"));
//    assertTrue(article.getTitle().equals("http://www.starmagazine.com/media/originals/Rihanna_1010_230.jpg"));
//  }

  public void testDailyBeast() {

    String url = "http://www.thedailybeast.com/blogs-and-stories/2010-11-01/ted-sorensen-speechwriter-behind-jfks-best-jokes/?cid=topic:featured1";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Legendary Kennedy speechwriter Ted Sorensen passed"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://www.tdbimg.com/files/2010/11/01/img-article---katz-ted-sorensen_163531624950.jpg"));
  }

  public void testBloomberg() {

    String url = "http://www.bloomberg.com/news/2010-11-01/china-becomes-boss-in-peru-on-50-billion-mountain-bought-for-810-million.html";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("The Chinese entrepreneur and the Peruvian shopkeeper"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://www.bloomberg.com/apps/data?pid=avimage&iid=iimODmqjtcQU"));
  }

  public void testScientificDaily() {

    String url = "http://www.scientificamerican.com/article.cfm?id=bpa-semen-quality";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("The common industrial chemical bisphenol A (BPA) "));
    assertTrue(article.getTopImage().getImageSrc().equals("http://www.scientificamerican.com/media/inline/bpa-semen-quality_1.jpg"));
    assertTrue(article.getTitle().equals("Everyday BPA Exposure Decreases Human Semen Quality"));
  }

  public void testSlamMagazine() {

    String url = "http://www.slamonline.com/online/nba/2010/10/nba-schoolyard-rankings/";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("When in doubt, rank players and add your findings"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://www.slamonline.com/online/wp-content/uploads/2010/10/celtics.jpg"));
    assertTrue(article.getTitle().equals("NBA Schoolyard Rankings"));
  }

  public void testTheFrisky() {

    String url = "http://www.thefrisky.com/post/246-rachel-dratch-met-her-baby-daddy-in-a-bar/?eref=RSS";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Rachel Dratch had been keeping the identity of her baby daddy "));
    assertTrue(article.getTopImage().getImageSrc().equals("http://cdn.thefrisky.com/images/uploads/rachel_dratch_102810_m.jpg"));
    assertTrue(article.getTitle().equals("Rachel Dratch Met Her Baby Daddy At A Bar"));
  }

  public void testUniverseToday() {

    String url = "http://www.universetoday.com/76881/podcast-more-from-tony-colaprete-on-lcross/";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("I had the chance to interview LCROSS"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://www.universetoday.com/wp-content/uploads/2009/10/lcross-impact_01_01.jpg"));
    assertTrue(article.getTitle().equals("More From Tony Colaprete on LCROSS"));
  }


  public void testCNBC() {

    String url = "http://www.cnbc.com/id/40491584";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("A prominent expert on Chinese works "));
    assertTrue(article.getTopImage().getImageSrc().equals("http://media.cnbc.com/i/CNBC/Sections/News_And_Analysis/__Story_Inserts/graphics/__ART/chinese_vase_150.jpg"));
    assertTrue(article.getTitle().equals("Chinese Art Expert 'Skeptical' of Record-Setting Vase"));
  }

  public void testEspnWithFlashVideo() {

    String url = "http://sports.espn.go.com/nfl/news/story?id=5971053";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("PHILADELPHIA -- Michael Vick missed practice Thursday"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://a.espncdn.com/i/espn/espn_logos/espn_red.png"));
    assertTrue(article.getTitle().equals("Michael Vick of Philadelphia Eagles misses practice, unlikely to play vs. Dallas Cowboys"));
  }

  public void testSportingNews() {

    String url = "http://www.sportingnews.com/nfl/feed/2011-01/nfl-coaches/story/raiders-cut-ties-with-cable";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("ALAMEDA, Calif. &mdash; The Oakland Raiders informed coach Tom Cable"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://dy.snimg.com/story-image/0/69/174475/14072-650-366.jpg"));
    assertTrue(article.getTitle().equals("Raiders cut ties with Cable"));
  }

  public void testFoxSports() {

    String url = "http://msn.foxsports.com/nfl/story/Tom-Cable-fired-contract-option-Oakland-Raiders-coach-010411";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("The Oakland Raiders informed coach Tom Cable"));
    assertTrue(article.getTitle().equals("Oakland Raiders won't bring Tom Cable back as coach - NFL News"));
  }

  public void testMSNBC() {

    String url = "http://www.msnbc.msn.com/id/41207891/ns/world_news-europe/";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Prime Minister Brian Cowen announced Saturday"));
    assertTrue(article.getTitle().equals("Irish premier resigns as party leader, stays as PM"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://msnbcmedia3.msn.com/j/ap/ireland government crisis--687575559_v2.grid-6x2.jpg"));
  }

  public void testEconomist() {

    String url = "http://www.economist.com/node/17956885";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("FOR beleaguered smokers, the world is an increasingly"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://media.economist.com/images/images-magazine/2011/01/22/st/20110122_stp004.jpg"));
  }


  public void testTheAtlantic() {

    String url = "http://www.theatlantic.com/culture/archive/2011/01/how-to-stop-james-bond-from-getting-old/69695/";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("If James Bond could age, he'd be well into his 90s right now"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://assets.theatlantic.com/static/mt/assets/culture_test/James%20Bond_post.jpg"));
  }

  public void testGawker() {

    String url = "http://gawker.com/#!5777023/charlie-sheen-is-going-to-haiti-with-sean-penn";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("With a backlash brewing against the incessant media"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://cache.gawkerassets.com/assets/images/7/2011/03/medium_0304_pennsheen.jpg"));
  }

  public void testNyTimes() {

    String url = "http://www.nytimes.com/2010/12/22/world/europe/22start.html";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("WASHINGTON &mdash; An arms control treaty paring back American"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://graphics8.nytimes.com/images/2010/12/22/world/22start-span/Start-articleInline.jpg"));
  }

  public void testTheVacationGals() {

    String url = "http://thevacationgals.com/vacation-rental-homes-are-a-family-reunion-necessity/";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Editors&rsquo; Note: We are huge proponents"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://thevacationgals.com/wp-content/uploads/2010/11/Gemmel-Family-Reunion-at-a-Vacation-Rental-Home1-300x225.jpg"));
  }

  // test the extraction of videos from a page
  public void testGettingVideosFromGraphVinyl() {
    String url = "http://grapevinyl.com/v/84/magnetic-morning/getting-nowhere";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getMovies().get(0).attr("src").equals("http://www.youtube.com/v/dsVWVtGWoa4&hl=en_US&fs=1&color1=d6d6d6&color2=ffffff&autoplay=1&iv_load_policy=3&rel=0&showinfo=0&hd=1"));

  }


  public void testShockYa() {

    String url = "http://www.shockya.com/news/2011/01/30/daily-shock-jonathan-knight-of-new-kids-on-the-block-publicly-reveals-hes-gay/";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("New Kids On The Block singer Jonathan Knight has publicly"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://www.shockya.com/news/wp-content/uploads/jonathan_knight_new_kids_gay.jpg"));
  }


  public void testLiveStrong() {

    String url = "http://www.livestrong.com/article/395538-how-to-decrease-the-rates-of-obesity-in-children/";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Childhood obesity increases a young person"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://photos.demandstudios.com/getty/article/184/46/87576279_XS.jpg"));
  }

  public void testLiveStrong2() {
    String url = "http://www.livestrong.com/article/396152-do-resistance-bands-work-for-strength-training/";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Resistance bands or tubes are named because"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://photos.demandstudios.com/getty/article/142/66/86504893_XS.jpg"));
  }

  public void testCracked() {
    String url = "http://www.cracked.com/article_19029_6-things-social-networking-sites-need-to-stop-doing.html";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Social networking is here to stay"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://i.crackedcdn.com/phpimages/article/2/1/5/45215.jpg?v=1"));
  }

  public void testTrailsCom() {
    String url = "http://www.trails.com/facts_41596_hot-spots-citrus-county-florida.html";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Snorkel and view artificial reefs or chase"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://cdn-www.trails.com/imagecache/articles/295x195/hot-spots-citrus-county-florida-295x195.png"));
  }

  public void testTrailsCom2() {
    String url = "http://www.trails.com/facts_12408_history-alpine-skis.html";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Derived from the old Norse word"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://cdn-www.trails.com/imagecache/articles/295x195/history-alpine-skis-295x195.png"));
  }

  public void testEhow() {
    String url = "http://www.ehow.com/how_7734109_make-white-spaghetti.html";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Heat the oil in the"));
    assertTrue(article.getTitle().equals("How to Make White Spaghetti"));
  }


  public void testGolfLink() {
    String url = "http://www.golflink.com/how_1496_eat-cheap-las-vegas.html";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("Las Vegas, while noted for its glitz"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://cdn-www.golflink.com/Cms/images/GlobalPhoto/Articles/2011/2/17/1496/fotolia4152707XS-main_Full.jpg"));
  }

   public void testAnswerBag() {
    String url = "http://www.answerbag.com/q_view/2438372";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("You're reading True or false"));

  }

   public void testAnswerBag2() {
    String url = "http://www.answerbag.com/q_view/2445112";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("You're reading Can chamomille"));

  }

  public void testTimeMagazine2() {
    String url = "http://www.time.com/time/specials/packages/article/0,28804,2065531_2065534,00.html";
    ContentExtractor contentExtractor = new ContentExtractor();
    Article article = contentExtractor.extractContent(url);
    assertTrue(article.getCleanedArticleText().startsWith("The traditional Passover song"));
    assertTrue(article.getTopImage().getImageSrc().equals("http://img.timeinc.net/time/photoessays/2011/top10_passover/scallions.jpg"));
  }

  public void testWSJ2() {
     String url = "http://online.wsj.com/article/SB10001424052748704004004576270433180029082.html?mod=WSJ_hp_LEFTTopStories";
     ContentExtractor contentExtractor = new ContentExtractor();
     Article article = contentExtractor.extractContent(url);
     assertTrue(article.getCleanedArticleText().startsWith("NEW YORK&mdash;U.S. stocks were on track"));
     assertTrue(article.getTopImage().getImageSrc().equals("http://m.wsj.net/video/20110418/041811marketshub2/041811marketshub2_512x288.jpg"));
   }

  public void testBuzznet() {
      String url = "http://wevegotyoucovered.buzznet.com/user/journal/8048821/buzznet-talks-bamboozle-festival-founder/";
      ContentExtractor contentExtractor = new ContentExtractor();
      Article article = contentExtractor.extractContent(url);
      assertTrue(article.getCleanedArticleText().startsWith("Bamboozle approaches! The three day music megafest"));
      assertTrue(article.getTopImage().getImageSrc().equals("http://cdn.buzznet.com/assets/imgx/1/4/0/6/1/1/9/1/orig-14061191.jpg"));
    }


  public void testTheSuperFicial() {
      String url = "http://www.thesuperficial.com/teen-mom-leah-divorce-corey-04-2011";
      ContentExtractor contentExtractor = new ContentExtractor();
      Article article = contentExtractor.extractContent(url);
      assertTrue(article.getCleanedArticleText().startsWith("Christ, did they all get implants?"));
      assertTrue(article.getTopImage().getImageSrc().equals("http://cdn03.cdn.thesuperficial.com/wp-content/uploads/2011/04/0418-teen-mom-leah-messer-divorce-14-480x720.jpg"));
    }






}

