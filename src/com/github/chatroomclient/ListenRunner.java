package com.github.chatroomclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Tristan on 6/06/2018.
 */
public class ListenRunner implements Runnable {
    InputStream is;
    ClientController clientController;

    public ListenRunner(InputStream is, ClientController c) {
        this.is = is;
        this.clientController = c;
    }

    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while (true) {
            try {
                line = reader.readLine();
                System.out.print("Line: ");
                System.out.println(line);
                //clientController.consoleLog(reader.readLine());
            } catch (IOException e) {
                System.out.println("Lost connection to the server...");
            }
        }
    }
}
