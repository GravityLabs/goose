package com.jimplush.goose.texthelpers;
/**
 * Created by IntelliJ IDEA.
 * User: robbie
 * Date: 5/13/11
 * Time: 12:11 AM
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class string {
  private static final Logger logger = LoggerFactory.getLogger(string.class);

  private string(){}

  public static final String empty = "";

  public static boolean isNullOrEmpty(String input) {
    if (input == null) return true;
    if (input.length() == 0) return true;
    return false;
  }
}


