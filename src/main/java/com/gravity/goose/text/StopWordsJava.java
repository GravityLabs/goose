package com.gravity.goose.text;

import com.gravity.goose.utils.FileHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jim
 * Date: 04.02.14
 * Time: 13:21
 * To change this template use File | Settings | File Templates.
 */
public class StopWordsJava
{
    // fixme replace scala calls
    private  StringReplacement PUNCTUATION = StringReplacement.compile("[^\\p{Ll}\\p{Lu}\\p{Lt}\\p{Lo}\\p{Nd}\\p{Pc}\\s]", "");
    String STOP_WORDS = FileHelper.loadResourceFile("stopwords-en.txt", StopWords.class()).split(sys.props("line.separator"));

    // fixme replace scala calls
    String removePunctuation(String str)
    {
        return PUNCTUATION.replaceAll(str);
    }

    // fixme replace scala calls
    public static WordStatsJava getStopWordCount(String content)
    {
        if(StringUtil.isNullOrEmpty(content))
        {
            return WordStatsJava.EMPTY();
        }
        WordStatsJava ws = new WordStatsJava();
        String strippedInput = removePunctuation(content);
        String[] candidateWords = StringUtil.SPACE_SPLITTER.split(strippedInput);
        List<String> overlappingStopWords = new ArrayList<String>();
        for(String w : candidateWords)
        {
            if(STOP_WORDS.contains(w.toLowerCase()))
            {
                overlappingStopWords.add(w.toLowerCase());
            }
        }
        ws.setWordCount(candidateWords.length);
        ws.setStopWordCount(overlappingStopWords.size());
        ws.setStopWords(overlappingStopWords);
        return ws;
    }

}
