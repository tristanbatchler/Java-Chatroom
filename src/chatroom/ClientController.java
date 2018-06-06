package chatroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

    // Initializer. Try to establish the socket connection between the client and the server.
    @FXML
    private void initialize(String name, String address, int port) {
        this.name = name;
        this.address = address;
        this.port = port;

        consoleLog("Connecting to " + address + ":" + port + " under user: " + name + "...");
        try {
            socket = connect(address, port);
            consoleLog("Success!");
        } catch (IOException e) {
            consoleLog("Failed due to an IO Exception.");
            consoleLog(e.getMessage());
            return;
        }
    }

    @FXML
    private void handleSendButtonAction(ActionEvent e) {
        sendMessage();
    }

    @FXML
    private void handleTextMessageKeyPress(KeyEvent k) {
        if (k.getCode() == KeyCode.ENTER) {
            sendMessage();
        } else {
            // The user is typing...
        }
    }

    @FXML
    private void handleCloseMenuItemAction(ActionEvent e) {
        // Close the window.
        Stage stage = (Stage) btnSend.getScene().getWindow();
        stage.close();
    }

    private Socket connect(String address, int port) throws IOException {
        Socket socket = new Socket(address, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        return socket;
    }

    private void sendMessage() {
        consoleLog(name, txtMessage.getText());
        clearMessage();
    }

    private void consoleLog(String name, String message) {
        txtConsole.setText(txtConsole.getText() + name + ": " + message + System.lineSeparator());
    }

    private void consoleLog(String message) {
        txtConsole.setText(txtConsole.getText() + message + System.lineSeparator());
    }

    private void clearMessage() {
        txtMessage.clear();
    }

}
