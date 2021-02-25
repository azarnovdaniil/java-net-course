package client;

import common.service.User;
import common.service.FileLoad;
import common.service.FileLoadService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    private HandlerCommand handlerCommand;

    private FileLoadService fileLoadService;


    @FXML
    VBox leftPanel, rightPanel;

    @FXML
    HBox authPanel, authError;


    @FXML
    TextField login, password, error;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handlerCommand = new HandlerCommand();
        fileLoadService = new FileLoadService();
        handlerCommand.init(Controller.this);
        leftPanel.setVisible(false);
        rightPanel.setVisible(false);
    }


    public void copyBtnAction() {
        PanelController leftPC = (PanelController) leftPanel.getProperties().get("ctrl");
        PanelController rightPC = (PanelController) rightPanel.getProperties().get("ctrl");

        if (leftPC.getSelectedFilename() == null && rightPC.getSelectedFilename() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ни один файл не был выбран", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        PanelController srcPC = null, dstPC = null;
        if (leftPC.getSelectedFilename() != null) {
            srcPC = leftPC;
            dstPC = rightPC;
        }
        if (rightPC.getSelectedFilename() != null) {
            srcPC = rightPC;
            dstPC = leftPC;
        }

        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFilename());
        Path dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPath.getFileName().toString());

        try {
            String srcPathStr = srcPath.toString();
            String dstPathStr = dstPath.toString();

            FileLoad fileLoad = new FileLoad(srcPathStr, dstPathStr);
            fileLoadService.readFile(fileLoad, handlerCommand.ctx);
            PrBar prBar = new PrBar();
            prBar.showAndWait();


            dstPC.updateList(Paths.get(dstPC.getCurrentPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось скопировать указанный файл", ButtonType.OK);
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateList() {
        PanelController leftPC = (PanelController) leftPanel.getProperties().get("ctrl");
        PanelController rightPC = (PanelController) rightPanel.getProperties().get("ctrl");
        leftPC.updateList(Paths.get(leftPC.getCurrentPath()));
        rightPC.updateList(Paths.get(rightPC.getCurrentPath()));
    }

    @FXML
    private void onConnectToServer(ActionEvent actionEvent) {
        StorageClient storageClient = new StorageClient();
        new Thread(() -> {
            try {

                storageClient.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void disconnectToServer(ActionEvent actionEvent) {

        handlerCommand.disconnect();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void click(ActionEvent actionEvent) {

    }

    public void auth() {
        error.clear();
        User user = new User(login.getText(), password.getText());
        handlerCommand.ctx.writeAndFlush(user);
        System.out.println("public void auth() " + user.toString());

    }

    public void authOk(boolean auth) {
        if (auth) {
            error.setVisible(false);
            error.setManaged(false);
            authPanel.setVisible(false);
            authPanel.setManaged(false);
            leftPanel.setVisible(true);
            rightPanel.setVisible(true);

        } else {
            error.setText("Неправильный логин или пароль, поробуйте снова");
        }
    }

    public void btnExitAction() {
        error.setVisible(true);
        error.setManaged(true);
        authPanel.setVisible(true);
        authPanel.setManaged(true);
        leftPanel.setVisible(false);
        rightPanel.setVisible(false);

    }

}
