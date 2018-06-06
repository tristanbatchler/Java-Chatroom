package com.github.chatroomserver;

/**
 * Created by Tristan on 6/06/2018.
 */
public class ServerMain {
    private int port;
    private Server server;

    public ServerMain(int port) {
        this.port = port;
        server = new Server(port);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar chat-server.jar [PORT]");
            return;
        }
        int port = Integer.parseInt(args[0]);
        new ServerMain(port);
    }
}
