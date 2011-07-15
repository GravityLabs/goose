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
package com.jimplush.goose.images;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.color.CMMException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class ImageUtils {


  private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

  /**
   * User: Jim Plush
   * gets the image dimensions for an image file, pass in the path to the image who's dimensions you want to get
   * this will use imageMagick since the Java IO and imaging shit SUCKS for getting mime types and file info for jpg and png files
   *
   * @param filePath
   * @return
   */
  public static ImageDetails getImageDimensions(String identifyProgram, String filePath) throws IOException {
    ArrayList<String> command = new ArrayList<String>(10);
    command.add(identifyProgram);
    command.add(filePath);

    // we should get a string back like: /Users/jim/Code/kelly.jpg JPEG 1024x768 1024x768+0+0 8-bit DirectClass 89.7KB 0.000u 0:00.000
    String imageInfo = execToString((String[]) command.toArray(new String[1]));

    ImageDetails imageDetails = new ImageDetails();

    if (imageInfo == null || imageInfo.contains("no decode delegate for this image format")) {
      throw new IOException("Unable to get Image Information (no decode delegate) for: " + filePath);
    }


    // let's break apart the returned line and set the properites in our map
    try {

      String[] infoParts = imageInfo.split(" ");

      imageDetails.setMimeType(infoParts[1]);

      String[] dimensions = infoParts[2].split("x");
      imageDetails.setWidth(Integer.parseInt(dimensions[0]));
      imageDetails.setHeight(Integer.parseInt(dimensions[1]));
      return imageDetails;


    } catch (NullPointerException e) {
      //logger.warn("Unable to get Image Information for: "+filePath + " INFO: "+imageInfo);
      throw new IOException("Unable to get Image Information for: " + filePath);
    }

  }

  /**
   * gets the image dimensions for an image file, pass in the path to the image who's dimensions you want to get, uses the built in java commands
   *
   * @param filePath
   * @return
   */
  public static HashMap<String, Integer> getImageDimensionsJava(String filePath) throws IOException {

    BufferedImage image = null;
    try {
      File f = new File(filePath);
      image = ImageIO.read(f);
      HashMap<String, Integer> results = new HashMap<String, Integer>();
      results.put("height", image.getHeight());
      results.put("width", image.getWidth());
      return results;

    } catch (CMMException e) {
      logger.error("ERROR READING FILE: " + filePath + " \n", e);
      throw new IOException("Unable to read file: " + filePath);
    } finally {
      if (image != null) {
        try {
          image.flush();
        } catch (Exception e) {
        }
      }
    }


  }

  /**
   * Tries to exec the command, waits for it to finish, logs errors if exit
   * status is nonzero, and returns true if exit status is 0 (success).
   *
   * @param command Description of the Parameter
   * @return Description of the Return Value
   */
  private static String execToString(String[] command) {
    Process p = null;
    BufferedReader in = null;
    try {
      p = Runtime.getRuntime().exec(command);
      in = new BufferedReader(
          new InputStreamReader(p.getInputStream()));
      String line = null;
      line = in.readLine();
      p.waitFor();

      return line;

    } catch (IOException e) {
      logger.error(e.toString(), e);
    } catch (InterruptedException e) {
      logger.error(e.toString(), e);
      throw new RuntimeException(e);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {

        }
      }
      if (p != null) {
        p.destroy();
      }
    }
    return null;


  }


}
