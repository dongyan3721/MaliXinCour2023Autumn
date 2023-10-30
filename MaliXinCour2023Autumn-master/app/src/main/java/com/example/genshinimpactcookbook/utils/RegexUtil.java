/**
 * @author Santa Antilles
 * @Time 2023-10-28 14:19
 */
/**
 * 这几个json解析器奇奇怪怪的，只能手写正则了
 */
package com.example.genshinimpactcookbook.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    private final static Pattern matchAlias = Pattern.compile("\"alias\": \"(.+?)\"");
    private final static Pattern matchPictureUrl = Pattern.compile("\"picuser\": \"(.+?)\"");
    private final static Pattern matchPreviewPictureUrl = Pattern.compile("\"picurl\": \"(.+?)\"");
    private final static Pattern matchContentUrl = Pattern.compile("\"playurl\": \"(.+?)\"");

    public static ArrayList<String> getAlias(String s){
        ArrayList<String> ret = new ArrayList<>();
        Matcher matcher = matchAlias.matcher(s);
        while(matcher.find()){
            ret.add(matcher.group(1));
        }
        return ret;
    }

    public static ArrayList<String> getPictureUrl(String s){
        ArrayList<String> ret = new ArrayList<>();
        Matcher matcher = matchPictureUrl.matcher(s);
        while(matcher.find()){
            ret.add(matcher.group(1));
        }
        return ret;
    }

    public static ArrayList<String> getPreviewPictureUrl(String s){
        ArrayList<String> ret = new ArrayList<>();
        Matcher matcher = matchPreviewPictureUrl.matcher(s);
        while(matcher.find()){
            ret.add(matcher.group(1));
        }
        return ret;
    }

    public static ArrayList<String> getContentUrl(String s){
        ArrayList<String> ret = new ArrayList<>();
        Matcher matcher = matchContentUrl.matcher(s);
        while(matcher.find()){
            ret.add(matcher.group(1));
        }
        return ret;
    }
}
