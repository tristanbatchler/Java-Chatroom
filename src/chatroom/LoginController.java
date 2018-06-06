package chatroom;

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
    private void handleLoginButtonAction(ActionEvent e) throws Exception {
        String name = txtName.getText();
        String address = txtAddress.getText();
        int port = Integer.parseInt(txtPort.getText());
        login(name, address, port);

        // Close this stage
        Stage loginStage = (Stage) txtName.getScene().getWindow();
        loginStage.close();

        // Open new stage
        FXMLLoader loader = new FXMLLoader(getClass().getResource("client.fxml"));
        Parent root = loader.load();

        ClientController controller = loader.getController();
        controller.name = name;
        controller.address = address;
        controller.port = port;

        Stage clientStage = new Stage();
        clientStage.setTitle("Chat Client");
        clientStage.setScene(new Scene(root, 640, 480));
        clientStage.show();
    }

    private void login(String name, String address, int port){
        System.out.println("Hello");
    }

}