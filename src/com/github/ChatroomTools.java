package com.github;

import com.github.chatroomclient.Client;

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

    public static Client getClientFromData(String data) {
        int beginningOfTailPos = data.length() - 2;
        for (; data.charAt(beginningOfTailPos) != '/'; beginningOfTailPos--);
        String[] meta = data.substring(beginningOfTailPos + 1, data.length() - 1).split("@");
        String name = meta[0];
        String address = meta[1].split(":")[0];
        int port = Integer.parseInt(meta[1].split(":")[1]);
        return new Client(name, address, port, null);
    }

}
