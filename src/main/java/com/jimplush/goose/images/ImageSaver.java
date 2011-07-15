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

import com.jimplush.goose.Configuration;
import com.jimplush.goose.network.HtmlFetcher;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Random;


/**
 * This class will be responsible for storing images to disk
 *
 * @author Jim Plush
 */
public class ImageSaver {

  private static final Logger logger = LoggerFactory.getLogger(ImageSaver.class);


  private static String getFileExtension(Configuration config, String fileName) throws IOException, SecretGifException {
    String fileExtension = "";
    String mimeType;
    try {

      ImageDetails imageDims = ImageUtils.getImageDimensions(config.getImagemagickIdentifyPath(), fileName);
      mimeType = imageDims.getMimeType();

      if (mimeType.equals("GIF")) {
        if (logger.isDebugEnabled()) {
          logger.debug("SNEAKY GIF! " + fileName);
        }
        throw new SecretGifException();
      }
      if (mimeType.equals("JPEG")) {
        fileExtension = ".jpg";
      } else if (mimeType.equals("PNG")) {
        fileExtension = ".png";
      } else {
        throw new IOException("BAD MIME TYPE: " + mimeType + " FILENAME:" + fileName);
      }

    } catch (SecretGifException e) {
      throw e;

    } catch (FileNotFoundException e) {
      logger.error(e.getMessage());
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw e;

    } finally {

    }

    return fileExtension;
  }


  /**
   * stores an image to disk and returns the path where the file was written
   *
   * @param imageSrc
   * @return
   */
  public static String storeTempImage(HttpClient httpClient, String linkhash, String imageSrc, Configuration config) throws SecretGifException {

    String localSrcPath = null;
    HttpGet httpget = null;
    HttpResponse response = null;

    try {

      imageSrc = imageSrc.replace(" ", "%20");
      if (logger.isDebugEnabled()) {
        logger.debug("Starting to download image: " + imageSrc);
      }

      HttpContext localContext = new BasicHttpContext();
      localContext.setAttribute(ClientContext.COOKIE_STORE, HtmlFetcher.emptyCookieStore);

      httpget = new HttpGet(imageSrc);

      response = httpClient.execute(httpget, localContext);

      String respStatus = response.getStatusLine().toString();
      if (!respStatus.contains("200")) {
        return null;
      }

      HttpEntity entity = response.getEntity();

      String fileExtension = "";
      try {
        Header contentType = entity.getContentType();
      } catch (Exception e) {
        logger.error(e.getMessage());

      }

      // generate random token
      Random generator = new Random();
      int randInt = generator.nextInt();

      localSrcPath = config.getLocalStoragePath() + "/" + linkhash + "_" + randInt;

      if (logger.isDebugEnabled()) {
        logger.debug("Storing image locally: " + localSrcPath);
      }
      if (entity != null) {
        InputStream instream = entity.getContent();
        OutputStream outstream = new FileOutputStream(localSrcPath);
        try {
          try {
            IOUtils.copy(instream, outstream);
          } catch (Exception e) {
            throw e;
          } finally {
            entity.consumeContent();
            instream.close();
            outstream.close();
          }

          // get mime type and store the image extension based on that shiz
          fileExtension = ImageSaver.getFileExtension(config, localSrcPath);
          if (fileExtension == "" || fileExtension == null) {
            if (logger.isDebugEnabled()) {
              logger.debug("EMPTY FILE EXTENSION: " + localSrcPath);
            }
            return null;
          }
          File f = new File(localSrcPath);
          if (f.length() < config.getMinBytesForImages()) {
            if (logger.isDebugEnabled()) {
              logger.debug("TOO SMALL AN IMAGE: " + localSrcPath + " bytes: " + f.length());
            }
            return null;
          }

          File newFile = new File(localSrcPath + fileExtension);
          f.renameTo(newFile);
          localSrcPath = localSrcPath + fileExtension;

          if (logger.isDebugEnabled()) {
            logger.debug("Image successfully Written to Disk");
          }

        } catch (IOException e) {
          logger.error(e.toString(), e);
        } catch (SecretGifException e) {
          throw e;
        } catch (Exception e) {
          logger.error(e.getMessage());
        }

      }

    } catch (IllegalArgumentException e) {
      logger.warn(e.getMessage());
    } catch (SecretGifException e) {
      raise(e);
    } catch (ClientProtocolException e) {

      logger.error(e.toString());
    } catch (IOException e) {
      logger.error(e.toString());
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.toString());
      e.printStackTrace();
    } finally {

      httpget.abort();

    }

    return localSrcPath;
  }


  private static void raise(SecretGifException e) {
    // TODO Auto-generated method stub

  }


}
