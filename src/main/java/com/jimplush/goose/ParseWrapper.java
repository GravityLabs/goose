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

  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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


  public static String now() {
    Calendar cal = Calendar.getInstance();
    return DATE_FORMAT.format(cal.getTime());

  }

}
