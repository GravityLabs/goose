package eu.ahref;

import com.jimplush.goose.Article;
import com.jimplush.goose.ContentExtractor;

/**
 * Created by IntelliJ IDEA.
 * User: martino
 * Date: 14/01/11
 * Time: 11.49
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public void testUrl(String url){
        ContentExtractor contentExtractor = new ContentExtractor();
        Article article = contentExtractor.extractContent(url);
        String title = article.getTitle();
        String text = article.getCleanedArticleText();
        System.out.println(title);
        System.out.println(text);

    }

    public static void main(String[] args){
        String valigiabluUrl = "http://http://www.valigiablu.it/doc/353/il-dramma-de-laquila-a-21-mesi-dal-sisma.htm";
        String foursquareUrl = "http://4sq.com/eHo5Wv";
        String twitterUrl = "http://bit.ly/fJYQwr";
        String yfrog = "http://yfrog.com/h0m0gygj";
        String laStampa = "http://tinyurl.com/36swakc";
        String Reuters = "http://flpbd.it/F0wf";
        String Corriere = "http://t.co/y7Ztgri";
        String testUrl = "http://twitter.com/#!/jayrosen_nyu/status/25729475291389952";
        Test test = new Test();
        test.testUrl(testUrl);
    }

}
