package com.example.genshinimpactcookbook;

import com.example.genshinimpactcookbook.utils.RegexUtil;

import org.junit.Test;

public class regexTest {
    @Test
    public void test_regex(){
        String s = "{\n" +
                "    \"id\": 1756,\n" +
                "    \"title\": \"看啥呢？小趴菜\uD83E\uDD28……嘻嘻\uD83D\uDE1C\",\n" +
                "    \"alias\": \"可可\uD83C\uDF80她是小坏蛋\",\n" +
                "    \"picuser\": \"https://vi1.6rooms.com/live/2023/03/17/15/1003v1679037442734330716.jpg\",\n" +
                "    \"picurl\": \"https://vi0.6rooms.com/live/2023/06/19/01/1032v1687109970956971134_l.jpg\",\n" +
                "    \"playurl\": \"https://minivideo.xiu123.cn/original/136790c00dff71ee80320764a3fc0102/4890539a-188cf9723cc.mp4\n" +
                "    \"sec\": \"8\"\n" +
                "}"+"{\n" +
                "    \"id\": 1756,\n" +
                "    \"title\": \"看啥呢？小趴菜\uD83E\uDD28……嘻嘻\uD83D\uDE1C\",\n" +
                "    \"alias\": \"可可\uD83C\uDF80她是小坏蛋\",\n" +
                "    \"picuser\": \"https://vi1.6rooms.com/live/2023/03/17/15/1003v1679037442734330716.jpg\",\n" +
                "    \"picurl\": \"https://vi0.6rooms.com/live/2023/06/19/01/1032v1687109970956971134_l.jpg\",\n" +
                "    \"playurl\": \"https://minivideo.xiu123.cn/original/136790c00dff71ee80320764a3fc0102/4890539a-188cf9723cc.mp4\n" +
                "    \"sec\": \"8\"\n" +
                "}";
        System.out.println(RegexUtil.getAlias(s));
    }
}
