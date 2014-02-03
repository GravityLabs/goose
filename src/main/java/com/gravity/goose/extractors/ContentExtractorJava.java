package com.gravity.goose.extractors;

import com.gravity.goose.ArticleJava;
import com.gravity.goose.text.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Collector;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;
import org.jsoup.select.TagsEvaluator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jim
 * Date: 03.02.14
 * Time: 1:41
 */
public class ContentExtractorJava
{
    private StringSplitter PIPE_SPLITTER = new StringSplitter("\\|");
    private StringSplitter ARROWS_SPLITTER = new StringSplitter("»");
    private StringSplitter DASH_SPLITTER = new StringSplitter(" - ");
    private StringSplitter COLON_SPLITTER = new StringSplitter(":");
    private StringSplitter SPACE_SPLITTER = new StringSplitter(" ");

    private StringReplacement MOTLEY_REPLACEMENT = new StringReplacement.compile("&#65533;", "");
    private ReplaceSequence TITLE_REPLACEMENT =  ReplaceSequence.create("&raquo;").append("»");
    private Set<String> NO_STRINGS = new HashSet<>();
    private String A_REL_TAG_SELECTOR = "a[rel=tag], a[href*=/tag/]";
    private String logPrefix = "ContentExtractor: ";

    TagsEvaluator TOP_NODE_TAGS = new TagsEvaluator(new HashSet("p", "td", "pre"));


    /**
     * Gets title string from article
     * fixme replace scala calls
     * @param article article to process
     * @return title string
     */
    String getTitle(ArticleJava article)
    {
        String title = "";
        Document doc = article.getDoc();
        try {
            Elements titleElem = doc.getElementsByTag("title");
            if(titleElem == null || titleElem.isEmpty())
            {
                return "";
            }

            String titleText = titleElem.first().text();

            if(titleText == null || titleElem.isEmpty())
            {
                return "";
            }
            boolean usedDelimeter = false;

            if(titleText.contains("|"))
            {
                // fixme
                titleText = doTitleSplits(titleText, PIPE_SPLITTER);
                usedDelimeter = true;
            }
            if(!usedDelimeter && titleText.contains("-"))
            {
                // fixme
                titleText = doTitleSplits(titleText, DASH_SPLITTER);
                usedDelimeter = true;
            }
            if(!usedDelimeter && titleText.contains("»"))
            {
                // fixme
                titleText = doTitleSplits(titleText, ARROWS_SPLITTER);
                usedDelimeter = true;
            }
            if(!usedDelimeter && titleText.contains(":"))
            {
                // fixme
                titleText = doTitleSplits(titleText, COLON_SPLITTER);
            }
            // fixme
            title = MOTLEY_REPLACEMENT.replaceAll(titleText);
        }
        catch(NullPointerException e) {
            // fixme
        }
        return title;
    }

    /**
     * based on a delimeter in the title take the longest piece or do some custom logic based on the site
     * fixme replace scala calls
     *
     * @param title title string
     * @param splitter desired splitter
     * @return splitted title
     */

    String doTitleSplits(String title, StringSplitter splitter)
    {
        int largestTextLen = 0;
        int largeTextIndex = 0;
        String[] titlePieces = splitter.split(title);

        int i = 0;
        while(i < titlePieces.length)
        {
            String current = titlePieces[i];
            if(current.length() > largestTextLen)
            {
                largestTextLen = current.length();
                largeTextIndex = i;
            }
            i++;
        }
        // fixme
        return TITLE_REPLACEMENT.replaceAll(titlePieces[largeTextIndex].trim());
    }

    /**
     * Returns metadata information from html document
     * @param doc document to process
     * @param metaName metatag name
     * @return metadata info
     */

    String getMetaContent(Document doc, String metaName)
    {
        Elements meta = doc.select(metaName);
        String content = null;

        if(meta.size() > 0)
        {
            content = meta.first().attr("content");
        }
        if(StringUtil.isNullOrEmpty(content))
        {
            return "";
        }
        else
        {
            return content.trim();
        }
    }

    /**
     * if the article has meta description set in the source, use that
     * @param article article to process
     * @return resulting metadata
     */

    String getMetaDescription(ArticleJava article)
    {
        return getMetaContent(article.getDoc(), "meta[name=description]");
    }

    /**
     * if the article has meta keywords set in the source, use that
     * @param article article to process
     * @return keywords
     */

    String getMetaKeywords(ArticleJava article)
    {
        return getMetaContent(article.getDoc(), "meta[name=keywords]");
    }

    /**
     * if the article has meta canonical link set in the url
     * fixme check logic against original code
     * @param article article to process
     * @return canonical link
     */

    String getCanonicalLink(ArticleJava article)
    {
        Elements meta = article.getDoc().select("link[rel=canonical]");
        if(meta.size() > 0)
        {
            // fixme check logic here
            String href = meta.first().attr("href").trim();
            if(!href.isEmpty())
            {
                return href;
            }
            else
            {
                article.getFinalUrl();
            }
        }
        else
        {
            article.getFinalUrl();
        }

        return "";
    }

    /**
     * returns domain from the url string
     * @param url url to process
     * @return domain name
     */

    String getDomain(String url)
    {
        String host = "";
        try
        {
            host = new URL(url).getHost();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();  //fixme to logger
        }
        return host;
    }

    /**
     * excract possible tags from article
     * fixme check logic against scala code
     * @param article article to process
     * @return set of tags
     */

    Set<String> extractTags(ArticleJava article)
    {
        Document node = article.getDoc();
        if (node.children().size() == 0)
        {
            return NO_STRINGS;
        }
        Elements elements = Selector.select(A_REL_TAG_SELECTOR, node);
        if(elements.size() == 0)
        {
            return NO_STRINGS;
        }

        HashSet<String> tags = new HashSet<>();
        for(Element el : elements)
        {
            String tag = el.text();
            if(StringUtil.isNullOrEmpty(tag))
            {
                tags.add(tag);
            }
        }
        return tags;
    }

    /**
     * we're going to start looking for where the clusters of paragraphs are. We'll score a cluster based on the number of stopwords
     * and the number of consecutive paragraphs together, which should form the cluster of text that this node is around
     * also store on how high up the paragraphs are, comments are usually at the bottom and should get a lower score
     * todo return type should be changed
     * @param article article to process
     * @return best score element
     */
    Element calculateBestNodeBasedOnClustering(ArticleJava article)
    {
        // fixme change to logger
        System.out.println(logPrefix + "Starting to calculate TopNode");
        Document doc = article.getDoc();
        Element topNode = null;

        Elements nodesToCheck = Collector.collect(TOP_NODE_TAGS, doc);
        double startingBoost = 1.0;
        int cnt = 0;
        int i = 0;
        HashSet<Element> parentNodes = new HashSet<>();
        ArrayList<Element> nodesWithText = new ArrayList<>();
        for(Element node : nodesToCheck)
        {
            String nodeText = node.text();
            //fixme remove scala code call
            WordStats wordStats = StopWords.getStopWordCount(nodeText);
            boolean highLinkDensity = isHighLinkDensity(node);
            if(wordStats.getStopWordCount() > 2 && !highLinkDensity)
            {
                nodesWithText.add(node);
            }
        }
        int numberOfNodes = nodesWithText.size();
        int negativeScoring = 0;
        double bottomNodesForNegativeScore = numberOfNodes * 0.25;
        // fixme change to logger
        System.out.println(logPrefix + "About to inspect num of nodes with text: " + numberOfNodes);
        for(Element node : nodesWithText)
        {
            float boostScore = 0;
            if(isOkToBoost(node))
            {
                if(cnt >= 0)
                {
                    boostScore = (float)((1.0 / startingBoost) * 50);
                    startingBoost++;
                }
            }
            if(numberOfNodes > 15)
            {
                if((numberOfNodes - i) <= bottomNodesForNegativeScore)
                {
                    float booster = (float)bottomNodesForNegativeScore;
                    boostScore = -(float)Math.pow(booster, 2.0);
                    float netScore = Math.abs(boostScore) + negativeScoring;
                    if(negativeScoring > 40)
                    {
                        boostScore = 5;
                    }
                }
            }

            // fixme change to logger
            System.out.println(logPrefix + "Location Boost Score: " + boostScore + " on interation: " + i + "' id='" + node.parent().id() + "' class='" + node.parent().attr("class"));
            String nodeText = node.text();
            WordStats wordStats = StopWords.getStopWordCount(nodeText);
            int upScore = (int)(wordStats.getStopWordCount() + boostScore);
            updateScore(node.parent(), upScore);
            updateScore(node.parent().parent(), upScore / 2);
            updateNodeCount(node.parent(), 1);
            updateNodeCount(node.parent().parent(), 1);
            if(!parentNodes.contains(node.parent()))
            {
                parentNodes.add(node.parent());
            }
            if(!parentNodes.contains(node.parent().parent()))
            {
                parentNodes.add(node.parent().parent());
            }
            cnt++;
            i++;
        }

        int topNodeScore = 0;
        for(Element e : parentNodes)
        {
            // fixme change to logger
            System.out.println(logPrefix + "ParentNode: score='" + e.attr("gravityScore") + "' nodeCount='" + e.attr("gravityNodes") + "' id='" + e.id() + "' class='" + e.attr("class") + "' ");
            int score = getScore(e);
            if(score > topNodeScore)
            {
                topNode = e;
                topNodeScore = score;
            }
            if(topNode == null)
            {
                topNode = e;
            }

        }

        printTraceLog(topNode);
        if(topNode == null)
        {
            return null;
        }
        else
        {
            return topNode;
        }
    }

    /**
     * @param topNode
     */

    void printTraceLog (Element topNode)
    {
        try
        {
            if(topNode != null)
            {
                // fixme change to logger
                System.out.println(logPrefix + "Our TOPNODE: score='" + topNode.attr("gravityScore") + "' nodeCount='" + topNode.attr("gravityNodes") + "' id='" + topNode.id + "' class='" + topNode.attr("class") + "' ");
                String text;

                if(topNode.text().trim().length() > 100)
                {
                    text = topNode.text().trim().substring(0, 100) + "...";
                }
                else
                {
                    text = topNode.text().trim();
                }
                // fixme change to logger
                System.out.println(logPrefix + "Text - " + text);
            }
        }
        catch (NullPointerException e)
        {
            e.printStackTrace(); // fixme change to logger
        }

    }

    /**
     * fixme stub
     * @param node
     * @return
     */

    private boolean isOkToBoost(Element node)
    {
        String para = "p";
        int stepsAway = 0;
        int minimumStopWordCount = 5;
        int maxStepsAwayFromNode = 3;

        for (Element currentNode:  walkSiblings(node))
        {
            if(currentNode.tagName() == para)
            {
                if(stepsAway >= maxStepsAwayFromNode)
                {
                    // fixme change to logger
                    System.out.println(logPrefix + "Next paragraph is too far away, not boosting");
                    return false;
                }
                String paraText = currentNode.text();
                // fixme replace scala code
                WordStats wordStats = StopWords.getStopWordCount(paraText);
                if(wordStats.getStopWordCount() > minimumStopWordCount)
                {
                    System.out.println(logPrefix + "We're gonna boost this node, seems contenty");
                    return true;
                }
                stepsAway++;
            }
        }
        return false;
    }

    /**
     *
     * @param e
     * @param max
     * @return
     */

    String getShortText(String e, int max)
    {
        if(e.length() > max)
        {
           return e.substring(0, max) + "...";
        }
        else
        {
            return e;
        }
    }

    /**
     * fixme remove scala code
     * @param e
     * @return
     */

    private boolean isHighLinkDensity (Element e)
    {
        Elements links = e.getElementsByTag("a");
        if(links.size() == 0)
        {
            return false;
        }
        String text = e.text().trim();
        // fixme
        String[] words = SPACE_SPLITTER.split(text);
        float numberOfWords = words.length;
        StringBuilder sb = new StringBuilder();
        for(Element link : links)
        {
            sb.append(link.text());
        }
        String linkText = sb.toString();
        // fixme
        String[] linkWords = SPACE_SPLITTER.split(linkText);
        float numberOfLinkWords = linkWords.length;
        float numberOfLinks = links.size();
        float linkDivisor = numberOfLinkWords / numberOfWords;
        float score = linkDivisor * numberOfLinks;
        //fixme change to logger
        System.out.println(logPrefix + "Calulated link density score as: " + score + " for node: " + getShortText(e.text, 50));
        if(score > 1)
        {
            return true;
        }
        return false;
    }

    /**
     * @param node
     * @return
     */

    private int getScore(Element node)
    {
        return getGravityScoreFromNode(node);
    }

    /**
     * @param node
     */
    int getGravityScoreFromNode(Element node)
    {
        String grvScoreString = node.attr("gravityScore");
        if(StringUtil.isNullOrEmpty(grvScoreString))
        {
            return 0;
        }
        return Integer.parseInt(grvScoreString);
    }

    /**
     * @param node
     * @param addToScore
     */

    private void updateScore(Element node, int addToScore)
    {
        int currentScore = 0;
        try
        {
            String scoreString = node.attr("gravityScore");
            if(StringUtil.isNullOrEmpty(scoreString))
            {
                currentScore = 0;
            }
            else
            {
                currentScore = Integer.parseInt(scoreString);
            }
        }
        catch (NumberFormatException e)
        {
            currentScore = 0;
        }
        int newScore = currentScore + addToScore;
        node.attr("gravityScore", Integer.toString(newScore));
    }

    /**
     * fixme stub
     * @param node
     * @param addToCount
     */

    private void updateNodeCount(Element node, int addToCount)
    {
        int currentScore = 0;
        try
        {
            String countString = node.attr("gravityNodes");
            if(StringUtil.isNullOrEmpty(countString))
            {
                currentScore = 0;
            }
            else
            {
                currentScore = Integer.parseInt(countString);
            }
        }
        catch (NumberFormatException e)
        {
            currentScore = 0;
        }
        int newScore = currentScore + addToCount;
        node.attr("gravityNodes", Integer.toString(newScore));
    }

    /**
     * fixme stub
     * @param node
     * @return
     */

    List<Element> extractVideos(Element node)
    {
        return null;
    }

    /**
     * fixme stub
     * @param e
     * @return
     */

    boolean isTableTagAndNoParagraphsExist(Element e)
    {
        return true;
    }

    Element postExtractionCleanup(Element targetNode)
    {
        return null;
    }

    /**
     * fixme return type
     * fixme stub
     * @param currentSibling
     * @param baselineScoreForSiblingParagraphs
     */
    void getSiblingContent(Element currentSibling, int baselineScoreForSiblingParagraphs)
    {

    }

    /**
     * fixme check logic
     * @param node
     */
    List<Element> walkSiblings(Element node)
    {
        Element currentSibling = node.previousElementSibling();
        List<Element> b = new LinkedList<>();

        while(currentSibling != null)
        {
            // fixme check logic
            b.add(currentSibling);
            // fixme change to logger
            System.out.println(logPrefix + "SIBLINGCHECK: " + debugNode(currentSibling));
            currentSibling = currentSibling != null ?  currentSibling.previousElementSibling() : null;
        }
        return b;
    }

    /**
     * fixme stub
     * @param topNode
     * @return
     */

    private Element addSiblings(Element topNode)
    {
        return null;
    }

    /**
     * fixme stub
     * @param topNode
     * @return
     */

    private int getBaselineScoreForSiblings(Element topNode)
    {
        return 0;
    }

    /**
     * fixme stub
     * fixme element ID
     * @param e
     * @return
     */
    String debugNode(Element e)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("GravityScore: '");
        sb.append(e.attr("gravityScore"));
        sb.append("' paraNodeCount: '");
        sb.append(e.attr("gravityNodes"));
        sb.append("' nodeId: '");
        //sb.append(e.);
        sb.append("' className: '");
        sb.append(e.attr("class"));
        return sb.toString();
    }
}
