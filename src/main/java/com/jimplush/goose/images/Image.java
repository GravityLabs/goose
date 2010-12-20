package com.jimplush.goose.images;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;

/**
 * User: jim
 * Date: 12/19/10
 */


public class Image {
  private static final Logger logger = Logger.getLogger(Image.class);

  /**
   * holds the Element node of the image we think is top dog
   */
  private Element topImageNode;

  /**
   * holds the src of the image
   */
  private String imageSrc;

  /**
   * how confident are we in this image extraction? the most images generally the less confident
   */
  private double confidenceScore = 0.0;


  /**
   * what kind of image extraction was used for this? bestGuess, linkTag, openGraph tags?
   */
  private String imageExtractionType = "";


  /**
   * stores how many bytes this image is.
   */
  private int bytes;

  public String getImageSrc() {
    return imageSrc;
  }

  public void setImageSrc(String imageSrc) {
    this.imageSrc = imageSrc;
  }

  public double getConfidenceScore() {
    return confidenceScore;
  }

  public void setConfidenceScore(double confidenceScore) {
    this.confidenceScore = confidenceScore;
  }

  public String getImageExtractionType() {
    return imageExtractionType;
  }

  public void setImageExtractionType(String imageExtractionType) {
    this.imageExtractionType = imageExtractionType;
  }

  public int getBytes() {
    return bytes;
  }

  public void setBytes(int bytes) {
    this.bytes = bytes;
  }

  public Element getTopImageNode() {
    return topImageNode;
  }

  public void setTopImageNode(Element topImageNode) {
    this.topImageNode = topImageNode;
  }
}
