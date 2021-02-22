package client;

import common.service.FileLoad;
import common.service.FileLoadService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        handlerCommand = new HandlerCommand();

        fileLoadService = new FileLoadService();

    }

    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }


    public void copyBtnAction(ActionEvent actionEvent) {
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

        handlerCommand.Disconnect();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void click(ActionEvent actionEvent) {

    }

}
