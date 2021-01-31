package ru.daniilazarnov;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CloudController implements Initializable {

    private String clientDir = "project/client/clientDir";

    public ListView<String> clientView;
    public ListView<String> serverView;

    public void upload(ActionEvent actionEvent) throws IOException {
        Network.get().getOut().writeUTF("/upload");
        String fileName = clientView.getSelectionModel().getSelectedItem();
        Network.get().getOut().writeUTF(fileName);
        File file = new File(clientDir + "/" + fileName);
        long size = file.length();
        Network.get().getOut().writeLong(size);
        byte[] buffer = new byte[255];
        FileInputStream fis = new FileInputStream(file);
        for (int i = 0; i < (size + 255) / 256; i++) {
            int read = fis.read(buffer);
            Network.get().getOut().write(buffer, 0, read);
        }
        Network.get().getOut().flush();
        fillServerData();
    }

    public void download(ActionEvent actionEvent) throws IOException {
        Network.get().getOut().writeUTF("/download");
        String fileName = serverView.getSelectionModel().getSelectedItem();
        Network.get().getOut().writeUTF(fileName);
        long size = Network.get().getIn().readLong();
        File file = new File(clientDir + "/" + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        byte [] buffer = new byte[256];
        for (int i = 0; i < (size + 255) / 256; i++) {
            if (i == (size + 255) / 256 - 1) {
                for (int j = 0; j < size % 256; j++) {
                    fos.write(Network.get().getIn().readByte());
                }
            } else {
                int read = Network.get().getIn().read(buffer);
                fos.write(buffer, 0, read);
            }
        }
        fos.close();
        fillClientData();
    }

    private void fillServerData() {
        try {
            serverView.getItems().clear();
            serverView.getItems().addAll(getServerFiles());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can not fill client files");
        }
    }

    private void fillClientData() {
        try {
            clientView.getItems().clear();
            clientView.getItems().addAll(getClientFiles());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can not fill client files");
        }
    }

    private List<String> getServerFiles() throws IOException {
        Network.get().getOut().write("ls".getBytes(StandardCharsets.UTF_8)); // was "/list"
//        int size = Network.get().getIn().readInt();
        int size = 2;
        ArrayList<String> serverFiles = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            System.out.println("Ждем!");
            System.out.println(Network.get().getIn().readUTF());
            serverFiles.add(Network.get().getIn().readUTF());
            System.out.println("Принял строку!");
        }
        System.out.println("Конец!");
        return serverFiles;
    }

    private List<String> getClientFiles() throws IOException {
        // ctrl + alt + v, cmd + opt + v
        Path clientDirPath = Paths.get(clientDir);
        // Files
        return Files.list(clientDirPath)
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillClientData();
        fillServerData();
    }
}
