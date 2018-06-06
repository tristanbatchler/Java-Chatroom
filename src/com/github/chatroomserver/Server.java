package com.github.chatroomserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Tristan on 6/06/2018.
 */
public class Server {
    private final static int MAX_CLIENTS = 64;

    private int port;
    private ServerSocket serverSocket;
    private Socket[] clientSockets = new Socket[MAX_CLIENTS];
    private int nClients = 0;
    String clientMessage;
    private boolean running = false;

    private Thread manageThread, sendThread, listen;

    public Server(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        running = true;
        System.out.println("Server started on " + port + ".");

        manageClients();
        listen();
    }


    private void manageClients() {
        manageThread = new Thread("Manage") {
            public void run() {
                while (running) {
                    try {
                        clientSockets[nClients] = serverSocket.accept();
                        nClients++;
                        System.out.println("Accepted client " + clientSockets[nClients - 1] + ".");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        manageThread.start();
    }

    private void listen() {
        listen = new Thread("Listen") {
            public void run() {
                System.out.println("Listening...");
                try {
                    for (Socket clientSocket : clientSockets) {
                        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
                        while (running) {
                            clientMessage = inFromClient.readLine();
                            System.out.println("Received: " + clientMessage);
                            outToClient.writeBytes(clientMessage);
                        }
                        clientSocket.close();
                        serverSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        listen.start();
    }

    private void send() {
        sendThread = new Thread("Send") {
            public void run() {
                /*
                while (running) {
                    // Sending
                }
                */
            }
        };
        sendThread.start();
    }
}
