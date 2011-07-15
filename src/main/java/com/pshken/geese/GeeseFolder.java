package com.pshken.geese;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;


public class GeeseFolder {
  
    private String folderDir;
    private final String[] EXT = {"html", "htm"};
    private FileUtils fUtils;
    private Collection collection;
    private File file;
    
    public GeeseFolder(){
        fUtils = new FileUtils();
    }
    
    public GeeseFolder(String folderDir){
        this.folderDir = folderDir;
        fUtils = new FileUtils();
        file = new File(folderDir);
    }
    
    public String[] listAllFile() throws Exception{
        String[] res; 
        if(file == null){
            throw new Exception("Please enter a directory");
        }else if(!this.isFolder()){
            throw new Exception("Invaild directory");
        }else{
            collection = fUtils.listFiles(file, EXT, true);
            res = new String[collection.size()];
            Iterator iter = collection.iterator();
            for(int i = 0; i < res.length; i++){
                res[i] = iter.next().toString();
            }
            return res;
        }
    }
    
    public void setFolderDir(String folderDir){
        this.folderDir = folderDir;
    }
    
    private boolean isFolder(){
        return file.isDirectory();
    }
}
