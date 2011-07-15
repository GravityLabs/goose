package com.pshken.geese;

import com.jimplush.goose.Article;
import com.jimplush.goose.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shaohong
 */
public class TalkToMeGeese{
    
    public void main(String[] args){
        try {
            File file = new File(args[0]);
            String html = "";
            Scanner scanner = new Scanner(file);
            
            while(scanner.hasNextLine()){
                html += scanner.nextLine();
            }
            
            Configuration config = new Configuration();
            config.setEnableImageFetching(false);
            GeeseContentExtractor contentExtractor = new GeeseContentExtractor(config);
            Article article = contentExtractor.extractContent(html);
            System.out.println(article.getCleanedArticleText().trim());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TalkToMeGeese.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
