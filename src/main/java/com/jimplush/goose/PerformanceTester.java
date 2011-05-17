package com.jimplush.goose;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.StopWatch;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jim Plush
 * User: jim
 * Date: 5/13/11
 */
public class PerformanceTester {


    /**
   * testing the performance of the extraction algos only
   * current best time on local macbook
   * run this 3 times and take the best time
   * 32329 ms for 100 articles v1
   * 27047 ms for 100 articles v2 (erraggy perf improvements)
   */
  public static void main(String[] args) {

    System.out.println("testing performance of general goose extraction algos");

    // make sure to set the logging level to "info"
    String html = PerformanceTester.getHTML("/com/jimplush/goose/statichtmlassets/scribd.txt");
    String url = "http://www.scribd.com/doc/52584146/Microfinance-and-Poverty-Reduction?in_collection=2987942";
    Configuration config = new Configuration();
    config.setEnableImageFetching(false);
    ContentExtractor contentExtractor = new ContentExtractor(config);

    StopWatch clock = new StopWatch();

    System.out.println("How long does it take to extract an article?");
    clock.start();
    for (int i = 0; i < 100; i++) {
      contentExtractor.extractContent(url, html);
    }
    clock.stop();

    System.out.println("It takes " + clock.getTime() + " milliseconds");

  }


  private static String getHTML(String filename) {
    String html = "";
    InputStream is = PerformanceTester.class.getResourceAsStream(filename);
    try {
      html = IOUtils.toString(is, "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return html;
  }
}
