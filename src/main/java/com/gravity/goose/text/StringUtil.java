package com.gravity.goose.text;

/**
 * Created with IntelliJ IDEA.
 * User: jim
 * Date: 03.02.14
 * Time: 18:35
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil
{
    static StringSplitter SPACE_SPLITTER = new StringSplitter(" ");
    public static boolean isNullOrEmpty(String input)
    {
        if(input == null)
        {
            return true;
        }
        if(input.length() == 0)
        {
            return true;
        }
        return false;
    }
}
