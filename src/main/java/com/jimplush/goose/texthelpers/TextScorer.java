package com.jimplush.goose.texthelpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextScorer {
  
  private static StringSplitter WHITESPACE_SPLITTER = new StringSplitter("\\s+");
  private static Pattern PUNCTUATION_PATTERN = Pattern.compile("[\\.,\\:;_\\(\\)\\[\\]\\?]");
  
  public static int score(String text) {
    String[] words = WHITESPACE_SPLITTER.split(text);
    int wordCount = words.length;
    
    Matcher matcher = PUNCTUATION_PATTERN.matcher(text);
    int punctuation = 0;
    while (matcher.find()) {
      punctuation++;
    }
    int sentenceEstimate = wordCount / 7;
    int punctuationPenalty = Math.min(20, Math.max(0, punctuation - wordCount / 3));
    return sentenceEstimate - punctuationPenalty;
  }
  
}
