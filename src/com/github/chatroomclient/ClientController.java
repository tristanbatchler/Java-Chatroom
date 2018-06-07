package com.github.chatroomclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientController {
    private String name, address;
    private int port;

    @FXML
    private TextArea txtConsole;
    @FXML
    private TextField txtMessage;
    @FXML
    private Button btnSend;
    @FXML
    private MenuItem miClose;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private boolean running = true;

    // Initializer. Try to establish the socket connection between the client and the server.
    @FXML
    void initialize(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;

        consoleLog("Connecting to " + address + ":" + port + " under user: " + name + "...");
        try {
            socket = connect(address, port);
            sendData("/LOGIN/");
            listen();
        } catch (IOException e) {
            consoleLog("Failed due to an IO Exception.");
            consoleLog(e.getMessage());
            return;
        }
    }

    @FXML
    private void handleSendButtonAction(ActionEvent e) {
        say();
    }

    @FXML
    private void handleTextMessageKeyPress(KeyEvent k) {
        if (k.getCode() == KeyCode.ENTER) {
            say();
        } else {
            // The user is typing...
        }
    }

    @FXML
    private void handleCloseMenuItemAction(ActionEvent event) {
        // Close the window.
        Stage stage = (Stage) btnSend.getScene().getWindow();
        stage.close();
        try {
            socket.close();
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Socket connect(String address, int port) throws IOException {
        socket = new Socket(InetAddress.getByName(address), port);
        writer = new PrintWriter(socket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return socket;
    }

    private void sendData(String data) throws IOException {
        writer.println(data);
    }

    private void say() {
        String message = txtMessage.getText();
        try {
            sendData("/SAY/" + message);
            clearMessage();
        } catch (IOException e) {
            consoleLog("Message failed to send (" + e.getMessage() + "). Please try again.");
        }
    }

    private void listen() throws IOException {
        Thread listenThread = new Thread("Listen") {
            public void run() {
                String message;
                try {
                    while ((message = reader.readLine()) != null) {
                        consoleLog(message);
                    }
                } catch (IOException e) {
                    consoleLog("Lost connection to the server...");
                }

            }
        };
        listenThread.start();
    }

    void consoleLog(String message) {
        txtConsole.setText(txtConsole.getText() + message + System.lineSeparator());
        System.out.println(message);
    }

    private void clearMessage() {
        txtMessage.clear();
    }

}
