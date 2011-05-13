package com.jimplush.goose.texthelpers;
/**
 * Created by IntelliJ IDEA.
 * User: robbie
 * Date: 5/13/11
 * Time: 3:53 PM
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class StringSplitter {
  private static final Logger logger = LoggerFactory.getLogger(StringSplitter.class);

  private Pattern pattern;

  public StringSplitter(String pattern) {
    this.pattern = Pattern.compile(pattern);
  }

  public String[] split(String input) {
    return pattern.split(input);
  }
}


