package com.jimplush.goose.images;

/**
 * holds the details of the result of inspecting an image
 * @author Jim Plush
 *
 */
public class ImageDetails {

  /**
   * the width of the image
   */
  private int width;
  
  /**
   * height of the image
   */
  private int height;
  
  /**
   * the mimeType of the image JPEG / PNG
   */
  private String mimeType;
  
  

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }
  
  
}
