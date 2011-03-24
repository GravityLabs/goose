package com.jimplush.goose;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import junit.framework.TestCase;

public class ProvidedHtmlTest extends TestCase {

	private String rawHtml;
	
	public void setUp(){	
		try {
			rawHtml = FileUtils.readFileToString(new File("testdata/3.html"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void testDefault(){
		
		ContentExtractor contentExtractor = new ContentExtractor();
	    String content = contentExtractor.extractContentFromHtmlString(rawHtml);
	    assertTrue(content.startsWith("Napster paved the way"));// very weak
	    
	    System.out.println(content);
	}
	

	
}
