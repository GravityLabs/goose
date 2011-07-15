package com.pshken.geese;

import com.jimplush.goose.Article;
import com.jimplush.goose.Configuration;
import java.io.File;
import java.util.Scanner;

/**
 *
 * @author shaohong
 */
public class GeeseRipper{
    
    private File file;
    private String html;
    private Scanner scanner;
    private boolean folder;
    
    private GeeseFolder folderDir;
    private Configuration config;
    private GeeseContentExtractor contentExtractor;
    private Article article;
    
    public GeeseRipper(String dir, boolean folder){
        html = "";
        config = new Configuration();
        this.folder = folder;
        if(folder == true){
            folderDir = new GeeseFolder(dir);
        }else{
            file = new File(dir);
        }
    }

    public GeeseRipper(){
        html = "";
        config = new Configuration();
    }
    
    
    /**
     * This is a rouge method, where it allows you to directly input html and
     * it will return you plain text. (This method is NOT RECOMMENDED to use)
     * 
     * @param html
     * @return 
     */
    public String extractText(String html){
        contentExtractor = new GeeseContentExtractor(config);
        Article article = contentExtractor.extractContent(html);
        return article.getCleanedArticleText().trim();
    }
    
    public String extractText() throws Exception{
        if(folder == true){
            throw new Exception("Invaild file.");
        }else{
            readFile();
            contentExtractor = new GeeseContentExtractor(config);
            Article article = contentExtractor.extractContent(html);
            return article.getCleanedArticleText().trim();
        }
    }
    
    public String[] extractTexts() throws Exception{
        if(folder == true){
            String[] listOfFiles = folderDir.listAllFile();
            String[] listOfPlainText = new String[listOfFiles.length];
            for(int i = 0; i < listOfFiles.length; i++){
                file = new File(listOfFiles[i]);
                readFile();
                contentExtractor = new GeeseContentExtractor(config);
                Article article = contentExtractor.extractContent(html);
                listOfPlainText[i] = article.getCleanedArticleText().trim();
            }
            return listOfPlainText;
        }else{
            throw new Exception("Invaild Folder");
        }

    }
    
    public void setFileDirectory(String fileDir){
        file = new File(fileDir);
    }
    
    private void readFile() throws Exception{
        scanner = new Scanner(file);
        while(scanner.hasNextLine()){
            html += scanner.nextLine();
        }
    }
    
}