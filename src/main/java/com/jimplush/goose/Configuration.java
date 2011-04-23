/**
 * Licensed to Gravity.com under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Gravity.com licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jimplush.goose;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: jim
 * Date: 12/19/10
 */

public class Configuration {
  private static final Logger logger = LoggerFactory.getLogger(Configuration.class);


  /**
   * this is the local storage path used to place images to inspect them
   */
  private String localStoragePath = "/opt/goose";


  /**
   * What's the minimum bytes for an image we'd accept is, alot of times we want to filter out the author's little images
   * in the beginning of the article
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
