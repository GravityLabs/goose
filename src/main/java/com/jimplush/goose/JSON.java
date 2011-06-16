package com.jimplush.goose;
import org.json.JSONObject;


public class JSON {

	public static void main(String[] args) {
		
		try {
			
			String url = args[0];
			
			Configuration configuration = new Configuration();
			configuration.setMinBytesForImages(500);
			
			ContentExtractor contentExtractor = new ContentExtractor(configuration);
			Article article = contentExtractor.extractContent(url);
			
			JSONObject json = new JSONObject();
			
			json.put("title", article.getTitle());
			json.put("date", article.getPublishDate());
			json.put("desc", article.getMetaDescription());
			json.put("keywords", article.getMetaKeywords());
			json.put("tags", article.getTags());
			json.put("movies", article.getMovies());
			json.put("images", article.getImageCandidates());
			json.put("image", article.getTopImage().getImageSrc());
			json.put("text", article.getCleanedArticleText());
			
			System.out.println(json.toString());
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}