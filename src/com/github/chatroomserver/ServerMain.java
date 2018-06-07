package com.github.chatroomserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Tristan on 6/06/2018.
 */
public class ServerMain {
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
                System.out.println("A new client is connected: " + clientSocket);

                // obtaining input and out streams
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // create a new thread object
                Thread t = new ClientManager(clientSocket, reader, writer);

                // Invoking the start() method
                t.start();
            } catch (IOException e) {
                clientSocket.close();
                e.printStackTrace();
            }
        }

    }
}
