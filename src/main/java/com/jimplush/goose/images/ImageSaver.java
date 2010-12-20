package com.jimplush.goose.images;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
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
import org.apache.log4j.Logger;


/**
 * This class will be responsible for storing images to disk
 * @author Jim Plush
 *
 */
public class ImageSaver {

  private static Logger logger = Logger.getLogger(ImageSaver.class);


  private static String getFileExtension(Configuration config, String fileName) throws IOException, SecretGifException
  {
    String fileExtension = "";
    String mimeType;
    try {
      
      ImageDetails imageDims = ImageUtils.getImageDimensions(config.getImagemagickIdentifyPath(), fileName);
      mimeType = imageDims.getMimeType();
      
      if(mimeType.equals("GIF")) {
        logger.info("SNEAKY GIF! "+fileName);
        throw new SecretGifException();
      }
      if(mimeType.equals("JPEG")) {
        fileExtension = ".jpg";
      } else if(mimeType.equals("PNG")) {
        fileExtension = ".png";
      } else {
        throw new IOException("BAD MIME TYPE: "+mimeType+" FILENAME:"+fileName);
      }

    } catch (SecretGifException e) {
      throw e;

    } catch (FileNotFoundException e) {
      logger.error(e.getMessage());
    } catch (IOException e) {
      logger.info(e.getMessage());
      throw e;
 
    } finally {
      
    }

    return fileExtension;
  }



  /**
   * stores an image to disk and returns the path where the file was written
   * @param imageSrc
   * @return
   */
  public static String storeTempImage(HttpClient httpClient, String linkhash, String imageSrc, Configuration config) throws SecretGifException
  {

    String localSrcPath = null;
    HttpGet httpget = null;
    HttpResponse response = null;

    try {

      imageSrc = imageSrc.replace(" ", "%20");     
      logger.info("Starting to download image: "+imageSrc);

      HttpContext localContext = new BasicHttpContext();
      localContext.setAttribute(ClientContext.COOKIE_STORE, HtmlFetcher.emptyCookieStore);

      httpget = new HttpGet(imageSrc);

      response = httpClient.execute(httpget, localContext);
      
      String respStatus = response.getStatusLine().toString();
      if(!respStatus.contains("200")) {
        return null;
      }

      HttpEntity entity = response.getEntity();

      String fileExtension = "";
      try {
        Header contentType = entity.getContentType();
      } catch(Exception e) {
        logger.info(e.getMessage());

      }

      // generate random token
      Random generator = new Random();
      int randInt = generator.nextInt();

      localSrcPath = config.getLocalStoragePath() + "/" + linkhash + "_" + randInt;

      logger.info("Storing image locally: "+localSrcPath);
      if (entity != null) { 
        InputStream instream = entity.getContent();
        OutputStream outstream = new FileOutputStream(localSrcPath);
        try {
          try {
            IOUtils.copy(instream, outstream);
          } catch(Exception e) {
            throw e;
          } finally {
            entity.consumeContent();
            instream.close();
            outstream.close();
          }

          // get mime type and store the image extension based on that shiz
          fileExtension = ImageSaver.getFileExtension(config, localSrcPath);
          if(fileExtension == "" || fileExtension == null) {
            logger.info("EMPTY FILE EXTENSION: "+localSrcPath);
            return null;
          }
          File f = new File(localSrcPath);
          if(f.length() < config.getMinBytesForImages()) {
            logger.info("TOO SMALL AN IMAGE: "+localSrcPath+ " bytes: "+f.length());
            return null;
          }

          File newFile = new File(localSrcPath+fileExtension);
          f.renameTo(newFile);
          localSrcPath = localSrcPath+fileExtension;

          logger.info("Image successfully Written to Disk");

        } catch(IOException e) {
          logger.info(e);
        } catch(SecretGifException e) {
          throw e;
        } catch(Exception e) {
          logger.error(e.getMessage());
        } 

      }

    } catch(IllegalArgumentException e) {
      logger.warn(e.getMessage());
    } catch(SecretGifException e) {
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
