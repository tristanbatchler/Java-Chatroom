package com.github.chatroomclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;

public class ClientController {

    @FXML
    private TextArea txtConsole;
    @FXML
    private TextField txtMessage;
    @FXML
    private Button btnSend;
    @FXML
    private MenuItem miClose;
    @FXML
    private ListView<Client> listUsers;

    private Client client;

    // Initializer. Try to establish the socket connection between the client and the server.
    @FXML
    void initialize(String name, String address, int port) {
        client = new Client(name, address, port, this);
    }

    @FXML
    private void handleSendButtonAction(ActionEvent e) {
        client.say(txtMessage.getText());
    }

    @FXML
    private void handleTextMessageKeyPress(KeyEvent k) {
        if (k.getCode() == KeyCode.ENTER) {
            client.say(txtMessage.getText());
        } else {
            // The user is typing...
        }
    }

    @FXML
    private void handleCloseMenuItemAction(ActionEvent event) {
        // Close the window.
        Stage stage = (Stage) btnSend.getScene().getWindow();
        stage.close();
        client.close();
    }

    void updateUserList(List<Client> userList) {
        listUsers.getItems().clear();
        listUsers.getItems().addAll(userList);
    }

    void consoleLog(String message) {
        txtConsole.setText(txtConsole.getText() + message + System.lineSeparator());
        System.out.println(message);
    }

    void clearMessage() {
        txtMessage.clear();
    }

}
