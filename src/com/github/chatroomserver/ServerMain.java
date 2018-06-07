package com.github.chatroomserver;

import com.github.ChatroomTools;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Created by Tristan on 6/06/2018.
 */
public class ServerMain {
    private static ArrayList<ServerClient> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java -jar chat-server.jar [PORT]");
            return;
        }

        int port = Integer.parseInt(args[0]);

        boolean running = true;
        System.out.println("Server started on " + port + ".");

        ServerSocket serverSocket = new ServerSocket(port);
        ServerClient client = null;
        while (running) {
            try {
                // Socket object to receive incoming client requests.
                client = new ServerClient("anonymous", serverSocket.accept());
                clients.add(client);

                System.out.println("New client connected: " + client);

                // Create a new thread for this client.
                ClientManager t = new ClientManager(client);
                t.start();
            } catch (IOException e) {
                client.socket.close();
                e.printStackTrace();
            }
        }
    }

    public static void process(ServerClient client, String data) {
        if (data.startsWith("/LOGIN/")) {
            client.name = ChatroomTools.getNameFromData(data);
        } else if (data.startsWith("/SAY/")) {

        } else {
            System.out.println("Unknown data: " + data + " not sent.");
        }
        send(data);
    }

    public static void send(String data) {
        for (ServerClient c : clients) {
            try {
                PrintWriter writer = new PrintWriter(c.socket.getOutputStream(), true);
                writer.println(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
