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
        String error =  "http://framethink.wordpress.com/2011/01/17/how-facebook-ships-code/";
        String errorb = "http://lhote.blogspot.com/2011/01/blindspot.html";
        String fuffa ="http://blog.flatlandia.eu/2010/09/nuitka-python-veloce.html";
        String asdasd="http://yfrog.com/h33rzlj";
        //  http://bit.ly/eMUNMD
        //  https://www.dropbox.com/dropquest2011
        //  https://www.dropbox.com/referrals/NTc4MTc5MDk?src=free_twitter
        //  http://lhote.blogspot.com/2011/01/blindspot.html
        //  http://framethink.wordpress.com/2011/01/17/how-facebook-ships-code/

        // http://nyti.ms/fEVBUI   guardare per l'immagine
        System.out.println("asdasdasdasdasdasd");
        Test test = new Test();
        test.testUrl(asdasd);
    }

}
