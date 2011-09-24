package com.gravity.goose

import org.junit.Test
import com.gravity.goose.extractors.AdditionalDataExtractor
import org.jsoup.nodes.Element

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/16/11
 * This class hits live websites and is only run manually, not part of the tests lifecycle
 */
class GoldSitesTestIT {

  @Test
  def techCrunch() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    //    implicit val config = TestUtils.NO_IMAGE_CONFIG
    val url = "http://techcrunch.com/2011/08/13/2005-zuckerberg-didnt-want-to-take-over-the-world/"
    val content = "The Huffington Post has come across this fascinating five-minute interview"
    val image = "http://tctechcrunch2011.files.wordpress.com/2011/08/screen-shot-2011-08-13-at-4-55-35-pm.png?w=288"
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
  def desertNews() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url = "http://www.deseretnews.com/article/705388385/High-school-basketball-Top-Utah-prospects-representing-well.html"
    val article = TestUtils.getArticle(url)
    val content = "In going up against some of the top AAU basketball teams in"
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
    val image = "http://si.wsj.net/public/resources/images/OB-JO747_stimul_G_20100814113803.jpg"
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
  def espn() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://sports.espn.go.com/espn/commentary/news/story?id=5461430"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "If you believe what college football coaches have said about sports",
      expectedImage = "http://a.espncdn.com/photo/2010/0813/ncf_i_mpouncey1_300.jpg")
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
      expectedImage = "http://cache.gawkerassets.com/assets/images/4/2011/08/medium_aftershock-earthquake-in-new-york-original.jpg")
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
      expectedImage = "http://timenewsfeed.files.wordpress.com/2011/08/newsfeed_0824.jpg?w=455")
  }

  @Test
  def cnet() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://news.cnet.com/8301-30686_3-20014053-266.html?tag=topStories1"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "NEW YORK--Verizon Communications is prepping a new",
      expectedImage = "http://i.i.com.com/cnwk.1d/i/tim//2010/08/18/Verizon_iPad_and_live_TV_610x458.JPG")
  }

  @Test
  def yahoo() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://news.yahoo.com/apple-says-steve-jobs-resigning-ceo-224628633.html"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "SAN FRANCISCO (AP) — Steve Jobs, the mind behind the iPhone",
      expectedImage = "http://l.yimg.com/bt/api/res/1.2/Q00X5.OHr6E5RB_IQnkCAQ--/YXBwaWQ9eW5ld3M7Y2g9MTc4Mjtjcj0xO2N3PTI2MTY7ZHg9MDtkeT0wO2ZpPXVsY3JvcDtoPTQzMDtxPTg1O3c9NjMw/http://media.zenfs.com/en_us/News/ap_webfeeds/c21f27410259ce13f60e6a706700a61a.jpg")
  }

  @Test
  def businessInsider() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.businessinsider.com/closing-bell-september-20-2011-9"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "And now we're looking at two down days in a",
      expectedImage = "http://static5.businessinsider.com/image/4df5d311ccd1d5591f190000-336-251/china-dive.jpg")
    TestUtils.printReport()
  }

  @Test
  def financialTimes() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.ft.com/intl/cms/s/2/4e268022-e472-11e0-92a3-00144feabdc0.htm"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "Hewlett-Packard shares jumped nearly 7 per",
      expectedImage = "http://im.media.ft.com/content/images/88de8b54-3f2e-11e0-8e48-00144feabdc0.img")
    TestUtils.printReport()
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
  def cnbc2() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.cnbc.com/id/44614459"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "Some traders found Wednesday's Fed statement to be a bit gloomier than expected.",
      expectedImage = "http://media.cnbc.com/i/CNBC/Sections/News_And_Analysis/__Story_Inserts/graphics/__FEDERAL_RESERVE/FED_RESERVE3.jpg")
    TestUtils.printReport()
  }

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
      expectedImage = "http://static6.businessinsider.com/image/4e77323e69beddba4c00001c-400-300/greece-flag-water.jpg")
    TestUtils.printReport()
  }

  @Test
  def businessinsider2() {
    val url = "http://www.businessinsider.com/goldman-on-the-fed-announcement-2011-9"
    implicit val config = TestUtils.DEFAULT_CONFIG
    val article = TestUtils.getArticle(url)

    TestUtils.runArticleAssertions(article = article,
      expectedStart = "From Goldman on the FOMC operation twist announcement",
      expectedImage = "http://static8.businessinsider.com/image/4e7a0dd26bb3f7da4800003d/twist.jpg")
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


}

