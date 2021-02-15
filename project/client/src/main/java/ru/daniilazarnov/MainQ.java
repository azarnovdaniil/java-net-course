package ru.daniilazarnov;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainQ extends Application {

    private final int height = 400;
    private final int weight = 400;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/client.fxml"));
            primaryStage.setTitle("Cloud Files");
            primaryStage.setScene(new Scene(root, height, weight));
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Ошибка");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
