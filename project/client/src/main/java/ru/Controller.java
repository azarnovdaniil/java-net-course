package ru;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import service.Item;

public class Controller{

    @FXML
    private ListView<Item> clientItemListView, storageItemListView;

    private StorageClient storageClient;

        @FXML
        private void onConnectToServer (ActionEvent actionEvent) {
            storageClient = new StorageClient();
            new Thread(() -> {
                try {

                    storageClient.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
}
