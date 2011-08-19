package com.jimplush.goose; /**
 * User: jim
 * Date: 1/22/11
 */

import junit.framework.TestCase;


public class ConfigurationTestIT extends TestCase {


  public void testConfigurationWorksWhenPassed() {


    // this is an example of using the configuration object with goose
    // it is expected for you to not want certain things in certain places so whatever is in the
    // configuration object you can override

    String url = "http://www.msnbc.msn.com/id/41207891/ns/world_news-europe/";

    // set my configuration options for goose
    Configuration configuration = new Configuration();
    configuration.setMinBytesForImages(5000);
    configuration.setLocalStoragePath("/opt/goose");
    configuration.setEnableImageFetching(false); // i don't care about the image, just want text, this is much faster!
    configuration.setImagemagickConvertPath("/opt/local/bin/convert");


//    ContentExtractor contentExtractor = new ContentExtractor(configuration);
//    Article article = contentExtractor.extractContent(url);
//    assertTrue(article.getCleanedArticleText().startsWith("Prime Minister Brian Cowen announced Saturday"));



  }


}
