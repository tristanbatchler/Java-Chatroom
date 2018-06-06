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
    private int port;
    private ServerSocket welcome;
    private Socket client;
    String clientMessage;
    private boolean running = false;

    private Thread manage, send, receive;

    public Server(int port) {
        this.port = port;
        try {
            welcome = new ServerSocket(port);
            client = welcome.accept();
            System.out.println("Accepted client " + client + ".");
        } catch (IOException e) {
            e.printStackTrace();
        }

        running = true;
        manageClients();
        receive();
    }


    private void manageClients() {
        manage = new Thread("Manage") {
            public void run() {
                /*
                while (running) {
                    // Managing
                }
                */
            }
        };
        manage.start();
    }

    private void receive() {
        receive = new Thread("Receive") {
            public void run() {
                System.out.println("Listening...");
                try {
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    DataOutputStream outToClient = new DataOutputStream(client.getOutputStream());
                    while (running) {
                        clientMessage = inFromClient.readLine();
                        System.out.println("Received: " + clientMessage);
                        outToClient.writeBytes(clientMessage);
                    }
                    client.close();
                    welcome.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        receive.start();
    }

    private void send() {
        send = new Thread("Send") {
            public void run() {
                /*
                while (running) {
                    // Sending
                }
                */
            }
        };
        send.start();
    }
}
