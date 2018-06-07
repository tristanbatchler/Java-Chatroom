package com.github.chatroomserver;

import java.io.*;

/**
 * Created by Tristan on 7/06/2018.
 */
public class ClientManager extends Thread {
    final private BufferedReader reader;
    final private ServerClient client;
    private boolean running = false;

    public ClientManager(ServerClient client) throws IOException{
        this.client = client;
        this.reader = new BufferedReader(new InputStreamReader(client.socket.getInputStream()));
    }

    @Override
    public void run() {
        running = true;
        String receivedLine;
        try {
            while ((receivedLine = reader.readLine()) != null) {
                System.out.println(receivedLine);
                ServerMain.process(client, receivedLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ServerClient " + client + " disconnected.");

        try {
            reader.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

}
