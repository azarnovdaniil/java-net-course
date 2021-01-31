package ru.daniilazarnov;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/cloud.fxml"));
        primaryStage.setTitle("Chat");
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }
}
