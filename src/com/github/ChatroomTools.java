package com.github;

/**
 * Created by Tristan on 7/06/2018.
 */
public class ChatroomTools {

    public static String stripMeta(String data) {
        int endOfHeadPos = 1;
        int beginningOfTailPos = data.length() - 2;

        for (; data.charAt(endOfHeadPos) != '/'; endOfHeadPos++);
        for (; data.charAt(beginningOfTailPos) != '/'; beginningOfTailPos--);

        return data.substring(endOfHeadPos + 1, beginningOfTailPos);
    }

    public static String getNameFromData(String data) {
        int beginningOfTailPos = data.length() - 2;
        for (; data.charAt(beginningOfTailPos) != '/'; beginningOfTailPos--);
        String meta = data.substring(beginningOfTailPos + 1, data.length() - 1);
        return meta.split("@")[0];
    }

}
