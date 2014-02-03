package com.gravity.goose;

import com.gravity.goose.images.ImageJava;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jim
 * Date: 03.02.14
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
public class ArticleJava
{
    String title = null;
    String cleanedArticleText = "";
    String metaDescription = "";
    String metaKeywords = "";
    String canonicalLink = "";
    String domain = "";
    Element topNode = null;
    ImageJava topImage = new ImageJava();
    Set<String> tags = null;
    List<String> movies = null;
    String finalUrl = "";
    String linkHash = "";
    String rawHtml = "";
    Document doc = null;
    Document rawDoc = null;
    Date publishDate = null;
    Map<String, String > additionalData = null;
}
