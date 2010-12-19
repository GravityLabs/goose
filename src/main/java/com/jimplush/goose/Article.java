package com.jimplush.goose; /**
 * User: jim plush
 * Date: 12/16/10
 */

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

/**
 * This class represents the extraction of an Article from a website
 *
 */
public class Article {

  private static final Logger logger = Logger.getLogger(Article.class);

  /**
   * Holds the title of the webpage
   */
  private String title;

  /**
   * holds the metadescription meta tag in the html doc
   */
  private String metaDescription;


  /**
   * holds the meta keywords that would in the meta tag of the html doc
   */
  private String metaKeywords;


  /**
   * holds the meta data canonical link that may be place in the meta tags of the html doc
   */
  private String canonicalLink;


  /**
   * holds the domain of where the link came from.
   * http://techcrunch.com/article/testme would be techcrunch.com as the domain
   */
  private String domain;

  /**
   * this represents the jSoup element that we think is the big content dude of this page
   * we can use this node to start grabbing text, images, etc.. around the content
   */
  private Element topNode;


  /**
   * holds a list of elements that related to youtube or vimeo movie embeds
   */
  private ArrayList<Element> movies;

  /**
   * returns the title of the webpage
   * @return
   */
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

    public String getMetaDescription() {
    return metaDescription;
  }

  public void setMetaDescription(String metaDescription) {
    this.metaDescription = metaDescription;
  }

  public String getMetaKeywords() {
    return metaKeywords;
  }

  public void setMetaKeywords(String metaKeywords) {
    this.metaKeywords = metaKeywords;
  }

  public String getCanonicalLink() {
    return canonicalLink;
  }

  public void setCanonicalLink(String canonicalLink) {
    this.canonicalLink = canonicalLink;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public Element getTopNode() {
    return topNode;
  }

  public void setTopNode(Element topNode) {
    this.topNode = topNode;
  }

  public ArrayList<Element> getMovies() {
    return movies;
  }

  public void setMovies(ArrayList<Element> movies) {
    this.movies = movies;
  }
}
