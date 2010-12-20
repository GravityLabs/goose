package com.jimplush.goose;

import org.apache.log4j.Logger;

/**
 * User: jim
 * Date: 12/19/10
 */

public class Configuration {
  private static final Logger logger = Logger.getLogger(Configuration.class);


  /**
   * this is the local storage path used to place images to inspect them
   */
  private String localStoragePath = "/opt/goose";


  /**
   * What's the minimum bytes for an image we'd accept is
   */
  private int minBytesForImages = 4500;


  /**
   * set this guy to false if you don't care about getting images, otherwise you can either use the default
   * image extractor to implement the ImageExtractor interface to build your own
   */
  private boolean enableImageFetching = true;


  /**
   * path to your imagemagick convert executable, on the mac using mac ports this is the default listed
   */
  private String imagemagickConvertPath = "/opt/local/bin/convert";


  /**
   *  path to your imagemagick identify executable
   */
  private String imagemagickIdentifyPath= "/opt/local/bin/identify";

  public String getLocalStoragePath() {
    return localStoragePath;
  }

  public void setLocalStoragePath(String localStoragePath) {
    this.localStoragePath = localStoragePath;
  }

  public int getMinBytesForImages() {
    return minBytesForImages;
  }

  public void setMinBytesForImages(int minBytesForImages) {
    this.minBytesForImages = minBytesForImages;
  }

  public boolean isEnableImageFetching() {
    return enableImageFetching;
  }

  public void setEnableImageFetching(boolean enableImageFetching) {
    this.enableImageFetching = enableImageFetching;
  }

  public String getImagemagickConvertPath() {
    return imagemagickConvertPath;
  }

  public void setImagemagickConvertPath(String imagemagickConvertPath) {
    this.imagemagickConvertPath = imagemagickConvertPath;
  }

  public String getImagemagickIdentifyPath() {
    return imagemagickIdentifyPath;
  }

  public void setImagemagickIdentifyPath(String imagemagickIdentifyPath) {
    this.imagemagickIdentifyPath = imagemagickIdentifyPath;
  }
}
