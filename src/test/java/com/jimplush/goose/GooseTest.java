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

    // set my configuration options for goose
    Configuration configuration = new Configuration();
    configuration.setMinBytesForImages(4500);
    configuration.setLocalStoragePath("/tmp/goose");
    configuration.setEnableImageFetching(false); // i don't care about the image, just want text, this is much faster!
    configuration.setImagemagickConvertPath("/opt/local/bin/convert");

    String url = "http://www.cnn.com/2010/POLITICS/08/13/democrats.social.security/index.html";
    Goose goose = new Goose(configuration);
    Article article = goose.extractContent(url);
  }

}
