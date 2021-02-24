package ru.daniilazarnov;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private Network network;

    @FXML
    private TextField msgField;

    @FXML
    private TextArea mainArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        network = new Network(args -> {
            mainArea.appendText((String) args[0]);
        });
    }

    public void sendMsgAction(ActionEvent actionEvent) {
        network.sendMessage(msgField.getText());
        msgField.clear();
        msgField.requestFocus();
    }

    public void exitAction(ActionEvent actionEvent) {
        network.close();
        Platform.exit();
    }
}
