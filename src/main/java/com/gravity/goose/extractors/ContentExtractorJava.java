package com.gravity.goose.extractors;

import com.gravity.goose.Article;
import com.gravity.goose.text.ReplaceSequence;
import com.gravity.goose.text.StringReplacement;
import com.gravity.goose.text.StringSplitter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.List;

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
    private StringReplacement MOTLEY_REPLACEMENT = new StringReplacement.compile("&#65533;", "");
    private ReplaceSequence TITLE_REPLACEMENT =  ReplaceSequence.create("&raquo;").append("»");


    /**
     * fixme replace scala calls
     * @param article
     * @return
     */
    String getTitle(Article article)
    {
        String title = "";
        Document doc = article.doc();
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
                titleText = doTitleSplits(titleText, PIPE_SPLITTER);
                usedDelimeter = true;
            }
            if(!usedDelimeter && titleText.contains("-"))
            {
                titleText = doTitleSplits(titleText, DASH_SPLITTER);
                usedDelimeter = true;
            }
            if(!usedDelimeter && titleText.contains("»"))
            {
                titleText = doTitleSplits(titleText, ARROWS_SPLITTER);
                usedDelimeter = true;
            }
            if(!usedDelimeter && titleText.contains(":"))
            {
                titleText = doTitleSplits(titleText, COLON_SPLITTER);
            }
            title = MOTLEY_REPLACEMENT.replaceAll(titleText);
        }
        catch(NullPointerException e) {

        }
        return title;
    }

    /**
     * fixme replace scala calls
     * @param title
     * @param splitter
     * @return
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
        return TITLE_REPLACEMENT.replaceAll(titlePieces[largeTextIndex].trim());
    }

    /**
     * fixme stub
     * @param doc
     * @param metaName
     * @return
     */

    String getMetaContent(Document doc, String metaName)
    {
        Elements meta = doc.select(metaName);
        String content = null;

        if(meta.size() > 0)
        {
            content = meta.first().attr("content");
        }
        if(content.isEmpty() || content == null)
        {
            return "";
        }
        else
        {
            return content.trim();
        }
    }

    /**
     * fixme replace scala call
     * @param article
     * @return
     */

    String getMetaDescription(Article article)
    {
        return getMetaContent(article.doc(), "meta[name=description]");
    }

    /**
     * fixme replace scala call
     * @param article
     * @return
     */

    String getMetaKeywords(Article article)
    {
        return getMetaContent(article.doc(), "meta[name=keywords]");
    }

    /**
     * fixme stub
     * @param article
     * @return
     */

    String getCanonicalLink(Article article)
    {
        return "";
    }

    /**
     * fixme stub
     * @param url
     * @return
     */

    String getDomain(String url)
    {
        return "";
    }

    /**
     * fixme stub
     * @param article
     * @return
     */

    HashSet<String> extractTags(Article article)
    {
        return null;
    }

    /**
     * todo return type should be changed
     * @param article
     * @return
     */
    String calculateBestNodeBasedOnClustering(Article article)
    {
        return "";
    }

    /**
     * fixme stub
     * @param topNode
     */

    void printTraceLog (Element topNode)
    {

    }

    /**
     * fixme stub
     * @param node
     * @return
     */

    boolean isOkToBoost(Element node)
    {
        return true;
    }

    /**
     * fixme stub
     * @param e
     * @return
     */

    private boolean isHighLinkDensity (Element e)
    {
        return true;
    }

    /**
     * fixme stub
     * @param node
     * @return
     */

    private int getScore(Element node)
    {
        return 0;
    }

    /**
     * todo return type should be changed
     * @param node
     */
    void getGravityScoreFromNode(Element node)
    {

    }

    /**
     * fixme stub
     * @param node
     * @param addToScore
     */

    private void updateScore(Element node, int addToScore)
    {

    }

    /**
     * fixme stub
     * @param node
     * @param addToCount
     */

    private void updateNodeCount(Element node, int addToCount)
    {

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
     * fixme return type
     * fixme stub
     * @param node
     */
    void walkSiblings(Element node)
    {

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
