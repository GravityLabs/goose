package com.jimplush.goose.texthelpers; /**
 * Created by IntelliJ IDEA.
 * User: robbie
 * Date: 5/13/11
 * Time: 11:38 AM
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringReplacement {
  private Matcher matcher;
  private String replaceWith;

  private StringReplacement(Matcher matcher, String replaceWith) {
    this.matcher = matcher;
    this.replaceWith = replaceWith;
  }

  public static StringReplacement compile(String pattern, String replaceWith) {
    if (string.isNullOrEmpty(pattern)) throw new IllegalArgumentException("Patterns must not be null or empty!");
    Matcher m = Pattern.compile(pattern).matcher(string.empty);
    return new StringReplacement(m, replaceWith);
  }

  public String replaceAll(String input) {
    matcher.reset(input);
    return matcher.replaceAll(replaceWith);
  }
}


