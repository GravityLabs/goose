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

    public String getImageSrc()
    {
        return imageSrc;
    }
}
