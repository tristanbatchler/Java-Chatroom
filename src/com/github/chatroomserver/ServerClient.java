package com.github.chatroomserver;

import java.net.Socket;

import java.io.*;
import java.time.LocalTime;

/**
 * Created by Tristan on 7/06/2018.
 */

public class ServerClient extends Thread {
    String name;
    Socket socket;
    final private BufferedReader reader;
    LocalTime lastResponseTime;
    ServerClient thisClient;

    public ServerClient(String name, Socket socket) throws IOException {
        this.name = name;
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.lastResponseTime = LocalTime.now();
        thisClient = this;
    }

    @Override
    public void run() {
        String receivedLine;
        try {
            while ((receivedLine = reader.readLine()) != null) {
                System.out.println("Receiving: " + receivedLine + " from " + thisClient);
                ServerMain.process(thisClient, receivedLine);
                lastResponseTime = LocalTime.now();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        ServerMain.kick(thisClient, "disconnected");
        System.out.println("Client " + thisClient + " disconnected.");
        try {
            closeReader();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    void closeReader() throws IOException {
        reader.close();
    }

    public String toString() {
        return name + "@" + socket.getInetAddress() + ":" + socket.getPort();
    }

}
