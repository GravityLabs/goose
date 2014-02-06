package com.gravity.goose.text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jim
 * Date: 04.02.14
 * Time: 13:18
 * To change this template use File | Settings | File Templates.
 */
public class WordStatsJava
{
    int stopWordCount = 0;
    int wordCount = 0;

    public static WordStatsJava EMPTY()
    {
        return new WordStatsJava();
    }
    List<String> stopWords = new ArrayList<>();

    public int getStopWordCount()
    {
        return stopWordCount;
    }

    public void setStopWordCount(int stopWordCount)
    {
        this.stopWordCount = stopWordCount;
    }

    public int getWordCount()
    {
        return wordCount;
    }

    public void setWordCount(int wordCount)
    {
        this.wordCount = wordCount;
    }

    public List<String> getStopWords()
    {
        return stopWords;
    }

    public void setStopWords(List<String> stopWords)
    {
        this.stopWords = stopWords;
    }
}
