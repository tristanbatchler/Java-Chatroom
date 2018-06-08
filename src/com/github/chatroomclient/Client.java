package com.github.chatroomclient;

import com.github.ChatroomTools;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Tristan on 7/06/2018.
 */
public class Client {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean running = true;
    private String name, address;
    private int port;
    private ClientController controller;

    public Client(String name, String address, int port, ClientController controller) {
        this.name = name;
        this.address = address;
        this.port = port;
        this.controller = controller;

        if (controller != null) {
            controller.consoleLog("Connecting to " + address + ":" + port + " under user: " + name + "...");
            try {
                socket = connect(address, port);
                sendData("/LOGIN//" + this + "/");
                sendData("/UPDATEUSERLIST//" + this + "/");
                listen();
            } catch (IOException e) {
                controller.consoleLog("Failed due to an IO Exception.");
                controller.consoleLog(e.getMessage());
                return;
            }
        }
    }

    Socket connect(String address, int port) throws IOException {
        socket = new Socket(InetAddress.getByName(address), port);
        writer = new PrintWriter(socket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return socket;
    }

    void sendData(String data) throws IOException {
        writer.println(data);
    }

    void say(String message) {
        try {
            sendData("/SAY/" + message + "/" + this + "/");
            controller.clearMessage();
        } catch (IOException e) {
            controller.consoleLog("Message failed to send (" + e.getMessage() + "). Please try again.");
        }
    }

    void listen() throws IOException {
        Thread listenThread = new Thread("Listen") {
            public void run() {
                String data;
                try {
                    while ((data = reader.readLine()) != null) {
                        String name = ChatroomTools.getNameFromData(data);
                        if (data.startsWith("/LOGIN/")) {
                            controller.consoleLog(name + " has joined the room.");
                        } else if (data.startsWith("/SAY/")) {
                            controller.consoleLog(name + ": " + ChatroomTools.stripMeta(data));
                        } else if (data.startsWith("/LOGOUT/")) {
                            controller.consoleLog(name + " has left the room.");
                        } else if (data.startsWith("/USERLIST/")) {
                            ArrayList<Client> clients = new ArrayList<>();

                            String clientsListString = ChatroomTools.stripMeta(data);
                            for (String clientString : clientsListString.substring(1, clientsListString.length() - 1).split(", ")) {
                                clients.add(getClientFromString(clientString));
                            }
                            Platform.runLater(
                                    () -> controller.updateUserList(clients)
                            );
                        } else if (data.startsWith("/PING/")) {
                            sendData("/PINGRESPONSE//" + this + "/");
                        }
                    }
                } catch (IOException e) {
                    controller.consoleLog("Lost connection to the server...");
                }

            }
        };
        listenThread.start();
    }

    void close() {
        try {
            sendData("/LOGOUT//" + this + "/");
            socket.close();
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return name + "@" + address + ":" + port;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Client)) {
            return false;
        }
        Client other = (Client)o;
        return other.name.equals(name) && other.address.equals(address) && other.port == port;
    }

    public static Client getClientFromString(String clientString) {
        // Of the form name@address:port
        String[] arr = clientString.split("@");
        String name = arr[0];
        String address = arr[1].split(":")[0];
        int port = Integer.parseInt(arr[1].split(":")[1]);
        return new Client(name, address, port, null);
    }
}
