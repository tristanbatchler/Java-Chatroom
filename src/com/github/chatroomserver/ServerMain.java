package com.github.chatroomserver;

import com.github.ChatroomTools;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Created by Tristan on 6/06/2018.
 */
public class ServerMain {
    private static ArrayList<ServerClient> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java -jar chat-server.jar [PORT]");
            return;
        }

        int port = Integer.parseInt(args[0]);

        boolean running = true;
        System.out.println("Server started on " + port + ".");

        ServerSocket serverSocket = new ServerSocket(port);

        // Search for and accept new clients
        Thread searchAndAcceptThread = new Thread("Search and Accept") {
            public void run() {
                ServerClient client = null;
                while (running) {
                    try {
                        // Socket object to receive incoming client requests.
                        client = new ServerClient("anonymous", serverSocket.accept());
                        clients.add(client);

                        System.out.println("New client connected: " + client);

                        // Run a new thread for this client.
                        client.start();
                    } catch (IOException e) {
                        //client.socket.close();
                        e.printStackTrace();
                    }
                }
            }
        };
        searchAndAcceptThread.start();


        // Manage clients
        Thread pingThread = new Thread("Ping") {
            public void run() {
                System.out.println("Pinging thread running.");
                while (true) {
                    if (!clients.isEmpty()) {
                        System.out.println("Searching for inactive users.");
                    }
                    /* Ping all clients with lastResponseTime more than 1 second ago and kick all clients
                     * with lastResponseTime more than 5 seconds ago. */
                    for (ServerClient c : clients) {
                        LocalTime now = LocalTime.now();
                        LocalTime oneSecondAgo = now.minusSeconds(1);
                        LocalTime fiveSecondsAgo = now.minusSeconds(5);

                        if (c.lastResponseTime.isBefore(oneSecondAgo)) {
                            send("/PING//server", c);
                        } else if (c.lastResponseTime.isBefore(fiveSecondsAgo)) {
                            kick(c, "timed out");
                        }
                    }
                    // Wait 1 second
                    try {
                        Thread.sleep(1000);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        pingThread.start();
    }

    static void process(ServerClient client, String data) {
        if (data.startsWith("/LOGIN/")) {
            client.name = ChatroomTools.getNameFromData(data);
            sendUserList();
            send(data);
        } else if (data.startsWith("/LOGOUT/")) {
            clients.remove(client);
            sendUserList();
            send(data);
        } else if (data.startsWith("/SAY/")) {
            send(data);
        }
    }

    private static void send(String data) {
        System.out.println("Sending: " + data + " to all.");
        for (ServerClient c : clients) {
            try {
                PrintWriter writer = new PrintWriter(c.socket.getOutputStream(), true);
                writer.println(data);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void send(String data, ServerClient c) {
        System.out.println("Sending: " + data + " to " + c + ".");
        try {
            PrintWriter writer = new PrintWriter(c.socket.getOutputStream(), true);
            writer.println(data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static void kick(ServerClient c, String reason) {
        try {
            c.closeReader();
        } catch (IOException e) {
            e.printStackTrace();
            c.interrupt();
        }
        send("/SAY/" + c.getName() + " has been kicked from the room. Reason: " + reason + "/server/");
        clients.remove(c);
        sendUserList();
    }

    private static void sendUserList() {
        send("/USERLIST/" + clients + "/server");
    }
}
