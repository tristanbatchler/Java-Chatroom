package com.github.chatroomserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Tristan on 7/06/2018.
 */
public class ClientManager extends Thread {
    final private BufferedReader reader;
    final private Socket socket;
    private boolean running = false;

    public ClientManager(Socket sockets) throws IOException{
        this.socket = sockets;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        running = true;
        String receivedLine;
        try {
            while ((receivedLine = reader.readLine()) != null) {
                System.out.println(receivedLine);
                ServerMain.sendMessage(receivedLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Client " + socket + " disconnected.");

        try {
            reader.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

}
