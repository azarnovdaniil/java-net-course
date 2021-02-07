package ru.daniilazarnov;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("resources/sample.fxml"));
        primaryStage.setTitle("Cloud");

        primaryStage.setScene(new Scene(root, 450, 350));
        primaryStage.show();

    }

    public static void main(String[] args) {


        System.out.println("Client!");

    }
}
