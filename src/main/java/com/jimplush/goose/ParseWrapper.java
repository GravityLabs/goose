package com.jimplush.goose;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * User: jim plush
 * Date: 12/16/10
 */

/**
 * This wrapper class is helpful when you start to multithread this bitch
 * You'll be able to see the url clearly that you were processing at the time
 * by viewing the stack dump of this class
 */
public class ParseWrapper {

  public String status = "notStarted";

  public String url;

  public String startTime;


  public Document parse(String html, String url) {
    this.url = url;
    this.status = "Started";
    this.startTime = now();
    Document doc;
    try {
      doc = Jsoup.parse(html);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    this.status = "Done";
    return doc;
  }
  
  public Document parse(String html){
	  
    this.status = "Started";
    this.startTime = now();
    Document doc;
    try {
      doc = Jsoup.parse(html);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    this.status = "Done";
    return doc;
  }


  public static String now() {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return sdf.format(cal.getTime());

  }

}
