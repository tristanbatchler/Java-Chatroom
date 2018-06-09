package com.github.chatroomclient;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField txtName, txtAddress, txtPort;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws Exception {
        String name = txtName.getText();
        String address = txtAddress.getText();
        int port = Integer.parseInt(txtPort.getText());
        login(name, address, port);

        // Close this stage
        Stage loginStage = (Stage) txtName.getScene().getWindow();
        loginStage.close();

        // Open new stage
        openClientStage(name, address, port);
    }

    private void openClientStage(String name, String address, int port) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("client.fxml"));
        Parent root = loader.load();

        ClientController controller = loader.getController();
        controller.initialize(name, address, port);

        Stage clientStage = new Stage();
        clientStage.setTitle("Chat ServerClient");
        clientStage.setScene(new Scene(root, 640, 480));

        //this makes all stages close and the app exit when the main stage is closed
        clientStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        clientStage.show();
    }

    private void login(String name, String address, int port){
        System.out.println("Hello");
    }

}
