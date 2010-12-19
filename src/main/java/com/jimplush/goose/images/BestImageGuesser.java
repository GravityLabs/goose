package com.jimplush.goose.images;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;

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
public class BestImageGuesser implements ImageExtractor{
  private static final Logger logger = Logger.getLogger(BestImageGuesser.class);

  public String getBestImage() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public ArrayList<Element> getAllImages() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
