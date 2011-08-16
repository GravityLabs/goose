package com.gravity.goose

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/16/11
 */


class Configuration {

  /**
  * this is the local storage path used to place images to inspect them, should be writable
  */
  private var localStoragePath: String = "/tmp/goose"
  /**
  * What's the minimum bytes for an image we'd accept is, alot of times we want to filter out the author's little images
  * in the beginning of the article
  */
  private var minBytesForImages: Int = 4500
  /**
  * set this guy to false if you don't care about getting images, otherwise you can either use the default
  * image extractor to implement the ImageExtractor interface to build your own
  */
  private var enableImageFetching: Boolean = true
  /**
  * path to your imagemagick convert executable, on the mac using mac ports this is the default listed
  */
  private var imagemagickConvertPath: String = "/opt/local/bin/convert"
  /**
  *  path to your imagemagick identify executable
  */
  private var imagemagickIdentifyPath: String = "/opt/local/bin/identify"

//  def getPublishDateExtractor: PublishDateExtractor = {
//    return publishDateExtractor
//  }



  /**
  * Pass in to extract article publish dates.
  * @param extractor a concrete instance of {@link PublishDateExtractor}
  * @throws IllegalArgumentException if the instance passed in is <code>null</code>
  */
//  def setPublishDateExtractor(extractor: PublishDateExtractor): Unit = {
//    if (extractor == null) throw new IllegalArgumentException("extractor must not be null!")
//    this.publishDateExtractor = extractor
//  }
//
//  def getAdditionalDataExtractor: AdditionalDataExtractor = {
//    return additionalDataExtractor
//  }

  /**
  * Pass in to extract any additional data not defined within {@link Article}
  * @param extractor a concrete instance of {@link AdditionalDataExtractor}
  * @throws IllegalArgumentException if the instance passed in is <code>null</code>
  */
//  def setAdditionalDataExtractor(extractor: AdditionalDataExtractor): Unit = {
//    this.additionalDataExtractor = extractor
//  }

  def getLocalStoragePath: String = {
    localStoragePath
  }

  def setLocalStoragePath(localStoragePath: String): Unit = {
    this.localStoragePath = localStoragePath
  }

  def getMinBytesForImages: Int = {
    minBytesForImages
  }

  def setMinBytesForImages(minBytesForImages: Int): Unit = {
    this.minBytesForImages = minBytesForImages
  }

  def isEnableImageFetching: Boolean = {
    enableImageFetching
  }

  def setEnableImageFetching(enableImageFetching: Boolean): Unit = {
    this.enableImageFetching = enableImageFetching
  }

  def getImagemagickConvertPath: String = {
    imagemagickConvertPath
  }

  def setImagemagickConvertPath(imagemagickConvertPath: String): Unit = {
    this.imagemagickConvertPath = imagemagickConvertPath
  }

  def getImagemagickIdentifyPath: String = {
    imagemagickIdentifyPath
  }

  def setImagemagickIdentifyPath(imagemagickIdentifyPath: String): Unit = {
    this.imagemagickIdentifyPath = imagemagickIdentifyPath
  }


//  private var publishDateExtractor: PublishDateExtractor = new PublishDateExtractor {
//    def extract(rootElement: Element): Date = {
//      return null
//    }
//  }
//  private var additionalDataExtractor: AdditionalDataExtractor = new AdditionalDataExtractor {
//    def extract(rootElement: Element): Map[String, String] = {
//      return null
//    }
//  }
}