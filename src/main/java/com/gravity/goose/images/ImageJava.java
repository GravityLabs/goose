package com.gravity.goose.images;

import org.jsoup.nodes.Element;

/**
 * Created with IntelliJ IDEA.
 * User: jim
 * Date: 03.02.14
 * Time: 14:42
 * To change this template use File | Settings | File Templates.
 */
public class ImageJava
{
    Element topImageNode = null;
    String imageSrc = "";
    double confidenceScore = 0.0;
    int height = 0;
    int width = 0;
    String imageExtractionType = "NA";
    long bytes = 0;

    public Element getTopImageNode()
    {
        return topImageNode;
    }

    public void setTopImageNode(Element topImageNode)
    {
        this.topImageNode = topImageNode;
    }

    public void setImageSrc(String imageSrc)
    {
        this.imageSrc = imageSrc;
    }

    public double getConfidenceScore()
    {
        return confidenceScore;
    }

    public void setConfidenceScore(double confidenceScore)
    {
        this.confidenceScore = confidenceScore;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public String getImageExtractionType()
    {
        return imageExtractionType;
    }

    public void setImageExtractionType(String imageExtractionType)
    {
        this.imageExtractionType = imageExtractionType;
    }

    public long getBytes()
    {
        return bytes;
    }

    public void setBytes(long bytes)
    {
        this.bytes = bytes;
    }

    public String getImageSrc()
    {
        return imageSrc;
    }
}
