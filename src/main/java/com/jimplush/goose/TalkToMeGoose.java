package com.jimplush.goose;

/**
 * Created by Jim Plush
 * User: jim
 * Date: 5/13/11
 */
public class TalkToMeGoose {

  /**
   * you can use this method if you want to run goose from the command line to extract html from a bashscript
   * or to just test it's functionality
   * you can run it like so
   * cd into the goose root
   * MAVEN_OPTS="-Xms256m -Xmx2000m" mvn exec:java -Dexec.mainClass=com.jimplush.goose.TalkToMeGoose  -Dexec.args="http://techcrunch.com/2011/05/13/native-apps-or-web-apps-particle-code-wants-you-to-do-both/" -e -q > ~/Desktop/gooseresult.txt
   *
   * Officer: [in the midst of the MIG battle] Both Catapults are broken, sir.
   * Stinger: How long will it take?
   * Officer: It'll take ten minutes.
   * Stinger: Bullshit ten minutes! This thing will be over in two minutes! Get on it!
   *
   * @param args
   */
  public static void main(String[] args) {

    try {

      String url = args[0];
      Configuration config = new Configuration();
      config.setEnableImageFetching(false);
      ContentExtractor contentExtractor = new ContentExtractor(config);
      Article article = contentExtractor.extractContent(url);
      System.out.println(article.getCleanedArticleText().trim());

    } catch (Exception e) {
      System.out.println("Make sure you pass in a valid URL: " + e.toString());
    }


  }

}
