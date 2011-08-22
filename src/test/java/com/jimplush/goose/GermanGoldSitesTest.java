package com.jimplush.goose;

import junit.framework.TestCase;

import com.jimplush.goose.images.Image;
import com.jimplush.goose.texthelpers.StopWords;


public class GermanGoldSitesTest extends TestCase {
  private static final Configuration DEFAULT_CONFIG = new Configuration();
  private static final Configuration NO_IMAGE_CONFIG = new Configuration();
  
  static {
    NO_IMAGE_CONFIG.setEnableImageFetching(false);
    NO_IMAGE_CONFIG.setStopWords(StopWords.DE);
    DEFAULT_CONFIG.setStopWords(StopWords.DE);
  }
  
  protected enum Site {
    SPIEGEL_ONLINE(
        "http://www.spiegel.de/netzwelt/web/0,1518,778965,00.html",
        "Nordkoreas Netz-Kriminelle: Die Hacker vom B&uuml;ro 39",
        "Sie sollen über Jahre hinweg Multiplayer-Rollenspiele manipuliert und Millionen Dollar erbeutet haben",
        "Für Nordkoreas Atomprogramm jedenfalls wären die Gelder der Gaming-Hacker wohl nicht mehr als ein Almosen.",
        "http://www.spiegel.de/images/image-64859-thumb-fxjx.jpg"),
    HEISE(
        "http://www.heise.de/newsticker/meldung/HP-Chef-Wir-erleben-einen-entscheidenden-Moment-in-unserer-Geschichte-1327391.html",
        "HP-Chef: &quot;Wir erleben einen entscheidenden Moment in unserer Geschichte&quot;",
        "Nach dem drastischen Kurseinbruch der Aktie von Hewlett-Packard will Konzernchef Léo Apotheker bei Investoren für seinen radikalen Umbauplan werben.",
        "Im vorbörslichen Handel in New York stieg der HP-Kurs um 1,44 Prozent.",
        null),
    WELT(
        "http://www.welt.de/kultur/kino/article13552679/Wenn-der-Dude-zum-kollektiven-Ommm-ruft.html",
        "&quot;The Big Lebowski&quot;: Wenn der Dude zum kollektiven &quot;Ommm!&quot; ruft",
        "Den Kulftfilm \"The Big Lebowski\" gibt es nun auch auf Blue-ray. Die Schauspieler samt Hauptdarsteller Jeff Bridges feierten den Termin als eine Art Klassentreffen.",
        "Darauf der Saal, begeistert und meditativ: „Ommm!“ Damit war alles gesagt.",
        null),
    BILD(
        "http://www.bild.de/geld/wirtschaft/rentner/660000-rentner-brauchen-neben-job-von-wegen-ruhestand-19522242.bild.html",
        "Von wegen Ruhestand: 660 000 Rentner brauchen Neben-Job",
        "Eine wachsende Zahl von Rentnern muss einem Zeitungsbericht zufolge zusätzlich arbeiten oder die staatliche Grundsicherung beantragen, um über die Runden zu kommen.",
        "teilte das Sozialministerium dem Bericht zufolge mit.",
        "http://bilder.bild.de/fotos/rentner-22430705-mfbq-19522260/Bild/2.bild.jpg"),
    SZ("http://www.sueddeutsche.de/sport/-fc-koeln-am-tabellenende-fussball-revolution-am-niederrhein-1.1133417",
        "Fu&szlig;ball-Revolution am Niederrhein",
        "Ein Wochenende genügt, um die Machtverhältnisse am Niederrhein zu erschüttern",
        "Und wenn's nur für einen einzigen Freitagabend ist.", // last space is non breaking
        null),
    TAGESSCHAU(
        "http://www.tagesschau.de/ausland/libyen1238.html",
        "Rebellen erreichen das Zentrum von Tripolis",
        "Die 42-jährige Herrschaft von Libyens Machthaber Muammar al Gaddafi steht offenbar kurz vor dem Zusammenbruch",
        "Südafrika dementierte die Berichte jedoch und teilte mit, Gaddafi habe nicht um Asyl gebeten.",
        null);
    
    private final String url, title, start, end, image;

    private Site(String url, String title, String start, String end) {
      this(url, title, start, end, null);
    }
    private Site(String url, String title, String start, String end, String image) {
      this.url = url;
      this.title = title;
      this.start = start;
      this.end = end;
      this.image = image;
    }
    
    public String getUrl() {
      return url;
    }

    public String getTitle() {
      return title;
    }

    public String getStart() {
      return start;
    }

    public String getEnd() {
      return end;
    }

    public String getImage() {
      return image;
    }
  }

  public void testSpiegelOnline() {
    Article article = getArticle(Site.SPIEGEL_ONLINE, true);
    
    runArticleAssertions(Site.SPIEGEL_ONLINE, article);
  }
  
  public void testHeise() {
    Article article = getArticle(Site.HEISE, false);
    
    runArticleAssertions(Site.HEISE, article);
  }

  public void testWelt() {
    Article article = getArticle(Site.WELT, false);
    
    runArticleAssertions(Site.WELT, article);
  }
  
  public void testBild() {
    Article article = getArticle(Site.BILD, true);

    runArticleAssertions(Site.BILD, article);
  }

  public void testSZ() {
    Article article = getArticle(Site.SZ, false);

    runArticleAssertions(Site.SZ, article);
  }
  
  public void testTagesschau() {
    Article article = getArticle(Site.TAGESSCHAU, false);


    runArticleAssertions(Site.TAGESSCHAU, article);
  }

  private Article getArticle(Site site, boolean fetchImages) {
    return getArticle(site, fetchImages ? DEFAULT_CONFIG : NO_IMAGE_CONFIG);
  }

  protected Article getArticle(Site site, Configuration config) {
    ContentExtractor extractor = new ContentExtractor(config);
    return extractor.extractContent(site.getUrl());
  }

  private static final char NL = '\n';
  private static final char TAB = '\t';
  private static StringBuilder tagReport = new StringBuilder("=======================::. TAG REPORT .::======================\n");

  private void runArticleAssertions(Site site, Article article) {
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

    String expectedTitle = site.getTitle();
    if (expectedTitle != null) {
      String title = article.getTitle();
      assertNotNull("Title was NULL!", title);
      assertEquals("Expected title was not returned!", expectedTitle, title);
    }

    String expectedStart = site.getStart();
    if (expectedStart != null) {
      String articleText = article.getCleanedArticleText();
      assertNotNull("Resulting article text was NULL!", articleText);
      assertTrue("Article text was not as long as expected beginning!", expectedStart.length() <= articleText.length());
      String actual = articleText.substring(0, expectedStart.length());
      assertEquals("The beginning of the article text was not as expected!", expectedStart, actual);
    }

    String expectedImage = site.getImage();
    if (expectedImage != null) {
      Image image = article.getTopImage();
      assertNotNull("Top image was NULL!", image);
      String src = image.getImageSrc();
      assertNotNull("Image src was NULL!", src);
      assertEquals("Image src was not as expected!", expectedImage, src);
    }

//    if (expectedDescription != null) {
//      String description = article.getMetaDescription();
//      assertNotNull("Meta Description was NULL!", description);
//      assertEquals("Meta Description was not as expected!", expectedDescription, description);
//    }
//
//    if (expectedKeywords != null) {
//      String keywords = article.getMetaKeywords();
//      assertNotNull("Meta Keywords was NULL!", keywords);
//      assertEquals("Meta Keywords was not as expected!", expectedKeywords, keywords);
//    }
    
    String expectedEnd = site.getEnd();
    if (expectedEnd != null) {
      String articleText = article.getCleanedArticleText();
      assertNotNull("Resulting article text was NULL!", articleText);
      assertTrue("Article text was not as long as expected ending!", expectedEnd.length() <= articleText.length());
      assertTrue(articleText.length() - expectedEnd.length() > 0);
      String actual = articleText.substring(articleText.length() - expectedEnd.length() - 2, articleText.length()).trim();
      assertEquals("The ending of the article text was not as expected!", expectedEnd, actual);
    }
  }

  protected static void printReport() {
    System.out.print(tagReport);
  }
}

