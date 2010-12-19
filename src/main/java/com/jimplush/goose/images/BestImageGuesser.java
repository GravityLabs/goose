package com.jimplush.goose.images;

import com.jimplush.goose.network.HtmlFetcher;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * User: jim plush
 * Date: 12/19/10
 */

/**
 * This image extractor will attempt to find the best image nearest the article.
 * Unfortunately this is a slow process since we're actually downloading the image itself
 * to inspect it's actual height/width and area metrics since most of the time these aren't
 * in the image tags themselves or can be falsified.
 * We'll weight the images in descending order depending on how high up they are compared to the top node content
 */
public class BestImageGuesser implements ImageExtractor {
  private static final Logger logger = Logger.getLogger(BestImageGuesser.class);

  /**
   * holds an httpclient connection object for doing head requests to get image sizes
   */
  HttpClient httpClient;

  /**
   * holds the document that we're extracting the image from
   */
  Document doc;


  /**
   * this lists all the known bad button names that we have
   */
  String regExBadImageNames;


  /**
   * holds the result of our image extraction
   */
  Image image;


  /**
   * the webpage url that we're extracting content from
   */
  String targetUrl;

  /**
   * What's the minimum bytes for an image we'd accept is
   */
  int minBytesForImages = 4500;



  public BestImageGuesser(HttpClient httpClient, String targetUrl) {
    this.httpClient = httpClient;

    StringBuilder sb = new StringBuilder();
    // create negative elements
    sb.append(".html|.gif|.ico|button|twitter.jpg|facebook.jpg|digg.jpg|digg.png|delicious.png|facebook.png|reddit.jpg|doubleclick|diggthis|diggThis|adserver|/ads/|ec.atdmt.com");
    sb.append("|mediaplex.com|adsatt|view.atdmt");
    this.regExBadImageNames = sb.toString();

    image = new Image();

    this.targetUrl = targetUrl;

  }

  public Image getBestImage(Document doc, Element topNode) {
    this.doc = doc;
    logger.info("Starting to Look for the Most Relavent Image");

    if (image.getImageSrc() == null) {
      this.checkForKnownElements();
    }


    return image;
  }

  public ArrayList<Element> getAllImages() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }


  /**
   * in here we check for known image contains from sites we've checked out like yahoo, techcrunch, etc... that have
   * known  places to look for good images.
   * //todo enable this to use a series of settings files so people can define what the image ids/classes are on specific sites
   */
  private void checkForKnownElements() {
    Element knownImage = null;

    logger.info("Checking for known images from large sites");
    String[] knownIds = {"yn-story-related-media", "cnn_strylccimg300cntr", "big_photo"};

    for (String knownName : knownIds) {
      try {
        Element known = this.doc.getElementById(knownName);

        if (known == null) {
          known = this.doc.getElementsByClass(knownName).first();
        }

        if (known != null) {
          Element mainImage = known.getElementsByTag("img").first();
          if (mainImage != null) {
            knownImage = mainImage;
            logger.info("Got Image: " + mainImage.attr("src"));
          }
        }

      } catch (NullPointerException e) {
        logger.info(e.toString(), e);
      }

    }

    if (knownImage != null) {
      this.image.setImageSrc(this.buildImagePath(knownImage.attr("src")));
      this.image.setImageExtractionType("known");
      this.image.setConfidenceScore(90);
      this.image.setBytes(this.getBytesForImage(knownImage.attr("src")));
    } else {
      logger.info("No known images found");
    }


  }

  /**
   * This method will take an image path and build out the absolute path to that image
   * using the initial url we crawled so we can find a link to the image if they use relative urls like ../myimage.jpg
   *
   * @param image
   * @return
   */
  private String buildImagePath(String image) {
    URL pageURL;
    String newImage = image.replace(" ", "%20");
    try {
      pageURL = new URL(this.targetUrl);
      URL imageURL = new URL(pageURL, image);

      newImage = imageURL.toString();
    } catch (MalformedURLException e) {
      logger.error("Unable to get Image Path: " + image);
    }
    return newImage;

  }


  /**
   * does the HTTP HEAD request to get the image bytes for this images
   *
   * @param src
   * @return
   */
  private int getBytesForImage(String src) {
    int bytes = 0;
    HttpHead httpget = null;
    try {
      String link = this.buildImagePath(src);
      link = link.replace(" ", "%20");

      HttpContext localContext = new BasicHttpContext();
      localContext.setAttribute(ClientContext.COOKIE_STORE, HtmlFetcher.emptyCookieStore);

      httpget = new HttpHead(link);

      HttpResponse response;

      response = httpClient.execute(httpget, localContext);

      HttpEntity entity = response.getEntity();

      bytes = this.minBytesForImages + 1;

      try {
        int currentBytes = (int) entity.getContentLength();
        Header contentType = entity.getContentType();
        if (contentType.getValue().contains("image")) {
          bytes = currentBytes;
        }
      } catch (NullPointerException e) {

      }

    } catch (Exception e) {

      logger.error(e.toString());
    } finally {
      try {
        httpget.abort();
      } catch (NullPointerException e) {
        logger.info("HttpGet is null, can't abortz");
      }
    }

    return bytes;
  }

}
