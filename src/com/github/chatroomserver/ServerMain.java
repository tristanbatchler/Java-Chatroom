package com.github.chatroomserver;

import com.sun.deploy.util.SessionState;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Tristan on 6/06/2018.
 */
public class ServerMain {

    private static ArrayList<Socket> clientSockets = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java -jar chat-server.jar [PORT]");
            return;
        }

        int port = Integer.parseInt(args[0]);

        boolean running = true;
        System.out.println("Server started on " + port + ".");

        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket = null;
        while (running) {
            try {
                // Socket object to receive incoming client requests.
                clientSocket = serverSocket.accept();
                clientSockets.add(clientSocket);
                System.out.println("A new client is connected: " + clientSocket);

                // create a new thread object
                ClientManager t = new ClientManager(clientSocket);

                // Invoking the start() method
                t.start();
            } catch (IOException e) {
                clientSocket.close();
                e.printStackTrace();
            }
        }
    }

    public static void sendMessage(String message){
        for (Socket s : clientSockets) {
            try {
                PrintWriter writer = new PrintWriter(s.getOutputStream(), true);
                writer.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
