package com.github.chatroomserver;

import java.net.Socket;

/**
 * Created by Tristan on 7/06/2018.
 */
public class ServerClient {
    String name;
    Socket socket;

    public ServerClient(String name, Socket socket) {
        this.name = name;
        this.socket = socket;
    }

    public String toString() {
        return name + "@" + socket.getInetAddress() + ":" + socket.getPort();
    }

}
