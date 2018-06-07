package com.github.chatroomserver;

import java.io.*;
import java.net.Socket;

/**
 * Created by Tristan on 7/06/2018.
 */
public class ClientManager extends Thread {
    final BufferedReader reader;
    final PrintWriter writer;
    final Socket socket;
    boolean running = false;

    public ClientManager(Socket socket, BufferedReader reader, PrintWriter writer) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public void run()
    {
        running = true;
        String receivedLine;
        while (running) {
            try {
                // receive the answer from client
                receivedLine = reader.readLine();
                System.out.println(receivedLine);
                writer.write(receivedLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // closing resources
            reader.close();
            writer.close();

        } catch(IOException e){
            e.printStackTrace();
        }
    }

}
