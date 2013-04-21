package com.gravity.goose

import org.junit.Test
import org.junit.Assert._
import com.gravity.goose.extractors.VoicesContentExtractor

/**
  * Created by Jim Plush
  * User: jim
  * Date: 8/16/11
  * This class hits live websites and is only run manually, not part of the tests lifecycle
  */
class GoldSitesTestIT {

  @Test
  def testArticleElementedArticle() { // to verify issue #56 is resolved
    implicit val config = TestUtils.NO_IMAGE_CONFIG
    val url = "http://www.repubblica.it/economia/2012/05/12/news/giovani_anziani_asili_nido_e_soldi_per_il_sud_ecco_il_progetto_del_governo_per_l_equit-34962952/"
    val content = "UN PIANO per l'equità e la crescita destinato in primo luogo al Sud. L'ha varato ieri il Consiglio dei ministri."
    val title = "Giovani, anziani, asili nido e soldi per il Sud ecco il progetto del governo per l'equità "
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article, expectedTitle = title, expectedStart = content)
    TestUtils.printReport()
  }

  @Test
  def techCrunch() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    //    implicit val config = TestUtils.NO_IMAGE_CONFIG
    val url = "http://techcrunch.com/2011/08/13/2005-zuckerberg-didnt-want-to-take-over-the-world/"
    val content = "The Huffington Post has come across this fascinating five-minute interview"
    val image = "http://tctechcrunch2011.files.wordpress.com/2011/08/screen-shot-2011-08-13-at-6-43-20-pm1.png?w=640"
    val title = "2005 Zuckerberg Didn’t Want To Take Over The World"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article, expectedTitle = title, expectedImage = image, expectedStart = content)
    TestUtils.printReport()
  }


  @Test
  def cnn() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url = "http://www.cnn.com/2010/POLITICS/08/13/democrats.social.security/index.html"
    val article = TestUtils.getArticle(url)
    val title = "Democrats to use Social Security against GOP this fall"
    val content = "Washington (CNN) -- Democrats pledged "
    val image = "http://i.cdn.turner.com/cnn/2010/POLITICS/08/13/democrats.social.security/story.kaine.gi.jpg"
    TestUtils.runArticleAssertions(article = article, expectedTitle = title, expectedStart = content, expectedImage = image)
    TestUtils.printReport()
  }

  @Test
  def cnn2() {
    val url = "http://www.cnn.com/2011/POLITICS/10/06/tea.party.left/index.html?hpt=hp_t1"
    implicit val config = TestUtils.DEFAULT_CONFIG
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "Washington (CNN) -- Wall Street should have seen it coming. After all, market forces were at work.",
      expectedImage = "http://i.cdn.turner.com/cnn/2011/POLITICS/10/06/tea.party.left/t1larg.occupydc2.jpg")
    TestUtils.printReport()
  }

  @Test
  def businessWeek() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.businessweek.com/magazine/content/10_34/b4192066630779.htm"
    val article: Article = TestUtils.getArticle(url)
    val title = "Olivia Munn: Queen of the Uncool"
    val content = "Six years ago, Olivia Munn arrived in Hollywood with fading ambitions of making it as a sports reporter and set about deploying"
    val image = "http://images.businessweek.com/mz/10/34/370/1034_mz_66popmunnessa.jpg"
    TestUtils.runArticleAssertions(article = article, expectedTitle = title, expectedStart = content, expectedImage = image)
  }


  @Test
  def businessWeek2() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.businessweek.com/magazine/content/10_34/b4192048613870.htm"
    val article: Article = TestUtils.getArticle(url)
    val content = "There's discord on Wall Street: Strategists at major American investment banks see a"
    val image = "http://images.businessweek.com/mz/covers/current_120x160.jpg"
    TestUtils.runArticleAssertions(article = article, expectedStart = content, expectedImage = image)

  }

  @Test
  def businessWeek3() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.businessinsider.com/ben-and-jerrys-schweddy-balls-one-million-moms-american-family-association-boycott-2011-9"
    val article: Article = TestUtils.getArticle(url)
    //    if (article == null) println("NULL ARTICLE!") else println("TEXT: \n" + article.cleanedArticleText)
    val content = "Not everyone's a fan of Ben & Jerry's new \"Schweddy Balls\" -- the Saturday Night Live-inspired flavor it rolled out a few weeks ago"
    val image = "http://static7.businessinsider.com/image/4e68c8c36bb3f7d80a000016/conservative-moms-are-now-calling-for-a-boycott-of-ben-and-jerrys-schweddy-balls-flavor.jpg"
    TestUtils.runArticleAssertions(article = article, expectedStart = content, expectedImage = image)

  }

  @Test
  def desertNews() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url = "http://www.deseretnews.com/article/705388385/High-school-basketball-Top-Utah-prospects-representing-well.html"
    val article = TestUtils.getArticle(url)
    val content = "Utah isn't known nationally for producing top basketball talent"
    TestUtils.runArticleAssertions(article, expectedStart = content)

  }

  @Test
  def foxNews() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.foxnews.com/politics/2010/08/14/russias-nuclear-help-iran-stirs-questions-improved-relations/"
    val article = TestUtils.getArticle(url)
    val content = "Russia's announcement that it will help Iran get nuclear fuel is raising questions"
    val image = "http://a57.foxnews.com/static/managed/img/Politics/396/223/startsign.jpg"
    TestUtils.runArticleAssertions(article = article, expectedStart = content, expectedImage = image)

  }

  @Test
  def foxNews2() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.foxnews.com/politics/2011/10/06/obama-defends-528-million-federal-loan-to-bankrupt-solyndra/"
    val article = TestUtils.getArticle(url)
    val content = "The director of the controversial loan program that cleared the way for a $535"
    val image = "http://a57.foxnews.com/static/managed/img/Politics/396/223/silver_jonathan.jpg"
    TestUtils.runArticleAssertions(article = article, expectedStart = content, expectedImage = image)
  }

  @Test
  def msnbc() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://bottomline.msnbc.msn.com/_news/2011/10/06/8190264-even-without-jobs-apple-still-shines-analysts-say"
    val article = TestUtils.getArticle(url)
    val content = "The death of technology titan Steve Jobs, co-founder and former CEO of Apple"
    val image = "http://msnbcmedia.msn.com/j/MSNBC/Components/Photo/_new/tz-biz-11106-applefuture-108p.nv_auth_landscape.jpg"
    TestUtils.runArticleAssertions(article = article, expectedStart = content, expectedImage = image)
  }


  @Test
  def laTimes() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.latimes.com/business/la-fi-jobs-legacy-hiltzik-20111006,0,5186643.column"
    val article = TestUtils.getArticle(url)
    val content = "Everyone knows Steve Jobs pulled off one of the outstanding corporate turnarounds in U.S. history"
    val image = "http://www.latimes.com/media/photo/2011-10/65235661.jpg"
    TestUtils.runArticleAssertions(article = article, expectedStart = content, expectedImage = image)
    TestUtils.printReport()

  }

  @Test
  def aolNews() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.aolnews.com/nation/article/the-few-the-proud-the-marines-getting-a-makeover/19592478"
    val article = TestUtils.getArticle(url)
    val content = "WASHINGTON (Aug. 13) -- Declaring \"the maritime soul of the Marine Corps\" is"
    val image = "http://o.aolcdn.com/photo-hub/news_gallery/6/8/680919/1281734929876.JPEG"
    TestUtils.runArticleAssertions(article = article, expectedStart = content, expectedImage = image)
  }

  @Test
  def wallStreetJournal() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://online.wsj.com/article/SB10001424052748704532204575397061414483040.html"
    val article = TestUtils.getArticle(url)
    val content = "The Obama administration has paid out less than a third of the nearly $230 billion"
    val image = "http://s.wsj.net/public/resources/images/OB-JO759_0814st_A_20100814143158.jpg"
    TestUtils.runArticleAssertions(article = article, expectedStart = content, expectedImage = image)
  }

  @Test
  def usaToday() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://content.usatoday.com/communities/thehuddle/post/2010/08/brett-favre-practices-set-to-speak-about-return-to-minnesota-vikings/1"
    val article = TestUtils.getArticle(url)
    val content = "Brett Favre couldn't get away from the"
    val image = "http://i.usatoday.net/communitymanager/_photos/the-huddle/2010/08/18/favrespeaksx-inset-community.jpg"
    TestUtils.runArticleAssertions(article = article, expectedStart = content, expectedImage = image)
  }

  @Test
  def usaToday2() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://content.usatoday.com/communities/driveon/post/2010/08/gm-finally-files-for-ipo/1"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "General Motors just filed with the Securities and Exchange ",
      expectedImage = "http://i.usatoday.net/communitymanager/_photos/drive-on/2010/08/18/cruzex-wide-community.jpg")
  }

  @Test
  def usaToday3() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.usatoday.com/money/perfi/funds/story/2011-10-05/3q-mutual-fund-report/50674776/1"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "Timothy McIntosh, a Tampa financial planner, has always been able to soothe his customers after a rough patch in the stock market. Until now.",
      expectedImage = "http://i.usatoday.net/money/_photos/2011/10/05/many-quit-stocks-is-it-time-to-buy-blen936-x.jpg")
    TestUtils.printReport()
  }


  @Test
  def espn() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://sports.espn.go.com/espn/commentary/news/story?id=5461430"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "If you believe what college football coaches have said about sports",
      expectedImage = "http://a.espncdn.com/photo/2010/0813/pg2_g_bush3x_300.jpg")
  }

  @Test
  def washingtonpost() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.washingtonpost.com/wp-dyn/content/article/2010/12/08/AR2010120803185.html"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "The Supreme Court sounded ",
      expectedImage = "http://media3.washingtonpost.com/wp-dyn/content/photo/2010/10/09/PH2010100904575.jpg")
  }

  @Test
  def gizmodo() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://gizmodo.com/5833746/what-if-the-earthquake-had-hit-manhattan"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "Today's 5.9 magnitude earthquake was felt throughout the Mid-Atlantic",
      expectedImage = "http://cache.gizmodo.com/assets/images/4/2011/08/fb_aftershock-earthquake-in-new-york-original.jpg")
  }

  @Test
  def engadget() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.engadget.com/2010/08/18/verizon-fios-set-top-boxes-getting-a-new-hd-guide-external-stor/"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "Streaming and downloading TV content to mobiles is nice",
      expectedImage = "http://www.blogcdn.com/www.engadget.com/media/2010/08/44ni600.jpg")
  }

  @Test
  def time() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.time.com/time/health/article/0,8599,2011497,00.html"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "This month, the federal government released",
      expectedImage = "http://img.timeinc.net/time/daily/2010/1008/bp_oil_spill_0817.jpg")
  }

  @Test
  def time2() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://newsfeed.time.com/2011/08/24/washington-monument-closes-to-repair-earthquake-induced-crack/"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "Despite what the jeers of jaded Californians might suggest",
      expectedImage = "http://timenewsfeed.files.wordpress.com/2011/08/newsfeed_0824.jpg?w=150")
  }

  @Test
  def time404() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://newsfeed.time.com/2011/08/24/washington-monument-closes-to-repair-earthquake-induced-FOO-BAR/"
    val article = TestUtils.getArticle(url)

    assertNull("Article title should be null for a 404 url!", article.title)
  }

  @Test
  def tulsaWorld() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.tulsaworld.com/site/articlepath.aspx?articleid=20111118_61_A16_Opposi344152&rss_lnk=7"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "Opposition to a proposal to remove certain personal data")

  }


  @Test
  def cnet() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://news.cnet.com/8301-30686_3-20014053-266.html?tag=topStories1"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "The phone company is adding bells and whistles to",
      expectedImage = "http://i.i.com.com/cnwk.1d/i/tim//2010/08/18/Verizon_iPad_and_live_TV_610x458.JPG")
  }

  @Test
  def wired() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.wired.com/epicenter/2011/10/steve-jobs-disability/"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "When I heard that Steve Jobs had passed away, I was boarding a train from New York to Philadelphia to visit my son.",
      expectedImage = "http://www.wired.com/images_blogs/business/2011/10/Apple-Siri-Blind-660x375.jpg")
  }

  @Test
  def msn() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://lifestyle.msn.com/your-life/your-money-today/article.aspx?cp-documentid=31244150"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "\"Head to the supermarket an hour before closing time. Some stores mark down ",
      expectedImage = "http://blu.stb.s-msn.com/i/6D/1235D306AF18A532BCDC8EB1CC42.jpg")
    TestUtils.printReport()
  }

  @Test
  def ap() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://hosted2.ap.org/APDEFAULT/bbd825583c8542898e6fa7d440b9febc/Article_2011-10-06-Kids-Concussions/id-6cb44517aaec4303936fa07d5490dce6"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "(AP) — The number of athletic children going to hospitals with concussions is up 60 percent in the past decade",
      expectedImage = null)
    TestUtils.printReport()
  }


  @Test
  def yahoo() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://news.yahoo.com/apple-says-steve-jobs-resigning-ceo-224628633.html"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "SAN FRANCISCO (AP) — Steve Jobs, the mind behind the iPhone",
      expectedImage = "http://l1.yimg.com/bt/api/res/1.2/rQjGYdY_uYh6LpCnzkGFvQ--/YXBwaWQ9eW5ld3M7Zmk9ZmlsbDtoPTc1O3E9ODU7dz0xMDA-/http://media.zenfs.com/en_us/News/ap_webfeeds/89854c5c8090bd15df0e6a706700dfbc.jpg")
  }

  @Test
  def abcnews() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://abcnews.go.com/Technology/steve-jobs-fire-company/story?id=14683754"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "Steve Jobs was just 30 years old, wildly successful, fabulously wealthy and a global celebrity. And then it all came crashing down.",
      expectedImage = "http://a.abcnews.com/images/Technology/gty_steve_jobs_port_4_dm_111006_wg.jpg")
  }

  @Test
  def businessInsider() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.businessinsider.com/closing-bell-september-20-2011-9"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "And now we're looking at two down days in a",
      expectedImage = "http://static7.businessinsider.com/image/4df5d311ccd1d5591f190000/major-rally-collapses-ahead-of-huge-day-heres-what-you-need-to-know.jpg")
    TestUtils.printReport()
  }

  @Test
  def financialTimes() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.ft.com/intl/cms/s/2/4e268022-e472-11e0-92a3-00144feabdc0.htm"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "Hewlett-Packard shares jumped nearly 7 per",
      expectedImage = null)
    TestUtils.printReport()
  }

  @Test
  def huffpoBusiness() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.huffingtonpost.com/david-macaray/labor-union-membership_b_973038.html"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "For men and women who plan on entering the job",
      expectedImage = null)
    TestUtils.printReport()
  }

  @Test
  def huffpo() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.huffingtonpost.com/2011/10/06/alabama-workers-immigration-law_n_997793.html"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "MONTGOMERY, Ala. -- Alabama's strict new immigration law may be backfiring.",
      expectedImage = "http://i.huffpost.com/gen/369284/thumbs/s-ALABAMA-WORKERS-IMMIGRATION-LAW-large.jpg")
    TestUtils.printReport()
  }


  @Test
  def huffpoBusiness2() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.huffingtonpost.com/2011/09/21/us-sees-challenges-in-s_n_974724.html"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "WASHINGTON (Reuters) - The government is continuing an aggressive drive to hold accountable",
      expectedImage = null)
    TestUtils.printReport()
  }

  @Test
  def nyTimes1() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    config.setBrowserUserAgent("grvGoose")
    val url: String = "http://www.nytimes.com/2011/09/20/arts/design/preserving-the-american-folk-art-museums-place-in-new-york.html?_r=1&ref=arts"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "Please. Someone, everyone, do something to save the American Folk Art Museum from dissolution and dispersa",
      expectedImage = "http://graphics8.nytimes.com/images/2011/09/20/arts/20folkart-web/20folkart-web-articleLarge.jpg")
    TestUtils.printReport()
  }

  @Test
  def nyTimes2() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    config.setBrowserUserAgent("grvGoose")
    val url: String = "http://www.nytimes.com/2011/10/07/health/07prostate.html?_r=1&hp"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "Healthy men should no longer receive a P.S.A. blood test to screen for prostate cancer because the test does not save lives",
      expectedImage = null)
    TestUtils.printReport()
  }


  @Test
  def gooseRequestParameters() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    config.setBrowserUserAgent("grvGoose")
    val url: String = "http://jimplush.com/public/uploads/goosetest.php"
    val article = TestUtils.getArticle(url)
    println(article.rawHtml)

  }


  @Test
  def cnbc() {
    implicit val config = TestUtils.NO_IMAGE_CONFIG

    val url: String = "http://www.cnbc.com/id/44613978"
    val article = TestUtils.getArticle(url)

    TestUtils.runArticleAssertions(article = article,
      expectedStart = "Some traders found Wednesday's Fed statement to be a bit gloomier than expected.")
    TestUtils.printReport()
  }

  @Test
  def cnbc3() {
    implicit val config = TestUtils.DEFAULT_CONFIG

    val url: String = "http://www.cnbc.com//id/44608735"
    val article = TestUtils.getArticle(url)

    TestUtils.runArticleAssertions(article = article,
      expectedStart = "Existing home sales rose more than expected in August to the fastest annual",
      expectedImage = "http://media.cnbc.com/i/CNBC/Sections/News_And_Analysis/__Story_Inserts/graphics/__REAL_ESTATE/home_sales13.jpg")
    TestUtils.printReport()
  }


  //  @Test
  //  def cnbc2() {
  //    // commented out while this issue is resolve: https://github.com/jhy/jsoup/issues/130
  //    implicit val config = TestUtils.DEFAULT_CONFIG
  //    val url: String = "http://www.cnbc.com/id/44614459"
  //    val article = TestUtils.getArticle(url)
  //    println(article.cleanedArticleText)
  //    TestUtils.runArticleAssertions(article = article,
  //      expectedStart = "Some traders found Wednesday's Fed statement to be a bit gloomier than expected.",
  //      expectedImage = "http://media.cnbc.com/i/CNBC/Sections/News_And_Analysis/__Story_Inserts/graphics/__FEDERAL_RESERVE/FED_RESERVE3.jpg")
  //    TestUtils.printReport()
  //  }

  @Test
  def yahooFinance() {
    val url = "http://finance.yahoo.com/news/Mulling-Meg-Whitman-HP-apf-4116866737.html?x=0"
    implicit val config = TestUtils.DEFAULT_CONFIG
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "SAN FRANCISCO (AP) -- As trial balloons go",
      expectedImage = "http://chart.finance.yahoo.com/instrument/1.0/HPQ/chart;range=1d/image;size=239x110?lang=en-US&region=US")
    TestUtils.printReport()
  }


  @Test
  def time3() {
    val url = "http://www.time.com/time/magazine/article/0,9171,804054,00.html"
    implicit val config = TestUtils.DEFAULT_CONFIG
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "The hemline could no longer be held. With wartime controls on",
      expectedImage = null)
    TestUtils.printReport()
  }


  @Test
  def yahooFinance2() {
    val url = "http://finance.yahoo.com/news/Stocks-plunge-after-Fed-apf-3386772167.html?x=0"
    implicit val config = TestUtils.DEFAULT_CONFIG
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "NEW YORK (AP) -- The Federal Reserve did what investors",
      expectedImage = "http://l.yimg.com/a/p/fi/41/20/44.jpg")
    TestUtils.printReport()
  }

  @Test
  def businessinsider() {
    val url = "http://www.businessinsider.com/meanwhile-developments-in-greece-2011-9"
    implicit val config = TestUtils.DEFAULT_CONFIG
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "As everyone in the world was transfixed on the Fed",
      expectedImage = "http://static5.businessinsider.com/image/4e77323e69beddba4c00001c/meanwhile-developments-in-greece.jpg")
    TestUtils.printReport()
  }

  @Test
  def businessinsider2() {
    val url = "http://www.businessinsider.com/goldman-on-the-fed-announcement-2011-9"
    implicit val config = TestUtils.DEFAULT_CONFIG
    val article = TestUtils.getArticle(url)

    TestUtils.runArticleAssertions(article = article,
      expectedStart = "From Goldman on the FOMC operation twist announcement",
      expectedImage = "http://static8.businessinsider.com/image/4e7a0dd26bb3f7da4800003d/goldman-4-key-points-on-the-fomc-announcement.jpg")
    TestUtils.printReport()
  }


  @Test
  def politico() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.politico.com/news/stories/1010/43352.html"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "If the newest Census Bureau estimates stay close to form",
      expectedImage = "http://images.politico.com/global/news/100927_obama22_ap_328.jpg")
  }

  @Test
  def buzznetImages() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://newageamazon.buzznet.com/user/journal/17025056/doubt-gives-hope-new-album/"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "We've had so many false hopes with the new No Doubt CD.",
      expectedImage = "http://img.buzznet.com/assets/imgx/2/0/8/2/2/2/1/3/orig-20822213.jpg")
  }

  @Test
  def timeImages() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://swampland.time.com/2012/01/09/hecklers-and-hostile-crowds-stymie-santorum-in-new-hampshire/"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "It was a scene fit for a front-runner: an overflow crowd spilling out the doors of a Rockwellian",
      expectedImage = "http://timeswampland.files.wordpress.com/2012/01/sl_santprotest_0109_blog.jpg?w=600&h=400&crop=1")
  }

  @Test
  def cnnMoneyImages() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://money.cnn.com/2012/01/09/pf/suze_orman_prepaid_card/index.htm?iid=HP_LN"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "NEW YORK (CNNMoney) -- CNBC's outspoken financial adviser, Suze",
      expectedImage = "http://i2.cdn.turner.com/money/2012/01/09/pf/suze_orman_prepaid_card/suze-orman.top.jpg")
  }

  @Test
  def yahooVoices() {
    implicit val config = {
      val myConfig = new Configuration
      myConfig.enableImageFetching = false
      myConfig.setContentExtractor(new VoicesContentExtractor)
      myConfig
    }
    val url: String = "http://voices.yahoo.com/article/9330101/lovess-demise-10882501.html"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article, expectedTitle = "Love's Demise",
      expectedStart = "Do we not love like lovers in demise? We both know our love has faded away;")
  }
}

