package com.jimplush.goose;

import com.gravity.goose.Goose;
import com.gravity.goose.Configuration;
import com.gravity.goose.Article;
import org.junit.Test;

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/19/11
 */
public class GooseTest {

  @Test
  public void gooseFromJavaTest() {
    String url = "http://www.cnn.com/2010/POLITICS/08/13/democrats.social.security/index.html";
    Goose goose = new Goose(new Configuration());
    Article article = goose.extractContent(url);
    System.out.println(article.title());


  }

}
