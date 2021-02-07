package ru.daniilazarnov;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.omg.IOP.Encoding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CloudController implements Initializable {

    private String clientDir = "project/client/clientDir";

    public ListView<String> clientView;
    public ListView<String> serverView;

    public void upload(ActionEvent actionEvent) throws IOException {
        String fileName = clientView.getSelectionModel().getSelectedItem();
        File file = new File(clientDir + "/" + fileName);
        long size = file.length();
        String sizeStr = String.valueOf(size);
        String uplCommand = "upload: " + fileName;
        Network.get().getOut().write(uplCommand.getBytes(StandardCharsets.UTF_8)); // was "/upload"
//        Network.get().getOut().writeLong(size);
//        byte[] buffer = new byte[255];
//        FileInputStream fis = new FileInputStream(file);
//        for (int i = 0; i < (size + 255) / 256; i++) {
//            int read = fis.read(buffer);
//            Network.get().getOut().write(buffer, 0, read);
//        }
//        Network.get().getOut().flush();
//        fillServerData();
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
        //Получение байта количества байт для числа количества файлов
        byte [] lengthNumberFiles = new byte[1];
        lengthNumberFiles[0] = Network.get().getIn().readByte();
        //Перевод байтов в строку
        String lengthNumberFilesStr = new String(lengthNumberFiles, "UTF-8");
        //Перевод строки в инт
        int lengthNumberFilesInt = Integer.parseInt(lengthNumberFilesStr);

        //Получение байтов числа количества файлов
        byte [] numberFiles = new byte[lengthNumberFilesInt];
        for (int i = 0; i < lengthNumberFilesInt; i++) {
            numberFiles[i] = Network.get().getIn().readByte();
        }
        String numberFilesStr = new String(numberFiles, "UTF-8");
        int numberFilesInt = Integer.parseInt(numberFilesStr);

        //Получение байтов названий файлов
        ArrayList<String> serverFiles = new ArrayList<>();
        for (int j = 0; j < numberFilesInt; j++) {
            //Получение байта количества байт для числа длины названия файла
            byte [] lengthOfLength = new byte[1];
            lengthOfLength[0] = Network.get().getIn().readByte();
            String lengthOfLengthStr = new String(lengthOfLength, "UTF-8");
            int lengthOfLengthInt = Integer.parseInt(lengthOfLengthStr);

            //Получение байтов числа длины названия файла
            byte [] userFileLength = new byte[lengthOfLengthInt];
            for (int i = 0; i < lengthOfLengthInt; i++) {
                userFileLength[i] = Network.get().getIn().readByte();
            }
            String userFileLengthStr = new String(userFileLength, "UTF-8");
            int userFileLengthInt = Integer.parseInt(userFileLengthStr);

            //Получение байтов названия файла
            byte[] arrBytes = new byte[userFileLengthInt];
            for (int i = 0; i < userFileLengthInt; i++) {
                arrBytes[i] = Network.get().getIn().readByte();
            }
            String userFile = new String(arrBytes, "UTF-8");
            System.out.println(userFile);
            //Добавление в список названия файла
            serverFiles.add(userFile);
        }
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
