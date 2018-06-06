package chatroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ClientController {
    String name, address;
    int port;

    @FXML
    TextArea txtConsole;
    @FXML
    TextField txtMessage;
    @FXML
    Button btnSend;
    @FXML
    MenuItem miClose;

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

    private void sendMessage() {
        consoleLog(name, txtMessage.getText());
        clearMessage();
    }

    private void consoleLog(String name, String message) {
        txtConsole.setText(txtConsole.getText() + name + ": " + message + System.lineSeparator());
    }

    private void clearMessage() {
        txtMessage.clear();
    }

}
