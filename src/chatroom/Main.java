package chatroom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage loginStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        loginStage.setTitle("Chat Login");
        loginStage.setResizable(false);
        loginStage.setScene(new Scene(root, 340, 500));
        loginStage.show();
    }
}
