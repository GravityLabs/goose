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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getCleanedArticleText()
    {
        return cleanedArticleText;
    }

    public void setCleanedArticleText(String cleanedArticleText)
    {
        this.cleanedArticleText = cleanedArticleText;
    }

    public String getMetaDescription()
    {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription)
    {
        this.metaDescription = metaDescription;
    }

    public String getMetaKeywords()
    {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords)
    {
        this.metaKeywords = metaKeywords;
    }

    public String getCanonicalLink()
    {
        return canonicalLink;
    }

    public void setCanonicalLink(String canonicalLink)
    {
        this.canonicalLink = canonicalLink;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public Element getTopNode()
    {
        return topNode;
    }

    public void setTopNode(Element topNode)
    {
        this.topNode = topNode;
    }

    public ImageJava getTopImage()
    {
        return topImage;
    }

    public void setTopImage(ImageJava topImage)
    {
        this.topImage = topImage;
    }

    public Set<String> getTags()
    {
        return tags;
    }

    public void setTags(Set<String> tags)
    {
        this.tags = tags;
    }

    public List<String> getMovies()
    {
        return movies;
    }

    public void setMovies(List<String> movies)
    {
        this.movies = movies;
    }

    public String getFinalUrl()
    {
        return finalUrl;
    }

    public void setFinalUrl(String finalUrl)
    {
        this.finalUrl = finalUrl;
    }

    public String getLinkHash()
    {
        return linkHash;
    }

    public void setLinkHash(String linkHash)
    {
        this.linkHash = linkHash;
    }

    public String getRawHtml()
    {
        return rawHtml;
    }

    public void setRawHtml(String rawHtml)
    {
        this.rawHtml = rawHtml;
    }

    public Document getDoc()
    {
        return doc;
    }

    public void setDoc(Document doc)
    {
        this.doc = doc;
    }

    public Document getRawDoc()
    {
        return rawDoc;
    }

    public void setRawDoc(Document rawDoc)
    {
        this.rawDoc = rawDoc;
    }

    public Date getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate(Date publishDate)
    {
        this.publishDate = publishDate;
    }

    public Map<String, String> getAdditionalData()
    {
        return additionalData;
    }

    public void setAdditionalData(Map<String, String> additionalData)
    {
        this.additionalData = additionalData;
    }
}
