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
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CloudController implements Initializable {

    private String clientDir = "project/client/clientDir";

    public ListView<String> clientView;
    public ListView<String> serverView;

    public void upload(ActionEvent actionEvent) throws IOException {
        String fileName = clientView.getSelectionModel().getSelectedItem();
        byte[] b = fileName.getBytes(StandardCharsets.UTF_8); //Нужно считать количество байт, а не символов, т.к. символ кириллицы имеет длинну - 2 байта, латиницы - 1 байт
        Network.get().getOut().writeByte((byte) 2);
        System.out.println("Sent byte = 2");
        Network.get().getOut().writeInt(b.length);
        Network.get().getOut().write(fileName.getBytes(StandardCharsets.UTF_8));
        File file = new File(clientDir + "/" + fileName);
        long size = file.length();
        System.out.println("Uploading file length - " + size);
        Network.get().getOut().writeLong(size);
        byte[] buffer = new byte[4];
        byte[] buffer1 = new byte[1];
        FileInputStream fis = new FileInputStream(file);
        for (int i = 0; i < (size + 3) / 4; i++) {
            // последний передаваемый буфер необходимо закидывать побайтово, т.к. целый буфер почему-то не докидывается в сеть
            if (i == (size + 3) / 4 - 1) {
                // передача последнего целого буфера
                if (size % 4 == 0) {
                    for (int j = 0; j < 4; j++) {
                        int read = fis.read(buffer1);
                        Network.get().getOut().write(buffer1, 0, read);
                    }
                    // передача последнего дробного буфера
                } else {
                    for (int j = 0; j < size % 4; j++) {
                        int read = fis.read(buffer1);
                        Network.get().getOut().write(buffer1, 0, read);
                    }
                }
                // передача целых буферов, кроме последнего
            } else {
                int read = fis.read(buffer);
                Network.get().getOut().write(buffer, 0, read);
            }
        }
        Network.get().getOut().flush();
        System.out.println("File sent");
        int g = Network.get().getIn().readInt();
        if (g == 2) {
            fillServerData();
        }
    }

    public void download(ActionEvent actionEvent) throws IOException {
        String fileName = serverView.getSelectionModel().getSelectedItem();
        byte[] b = fileName.getBytes(StandardCharsets.UTF_8); //Нужно считать количество байт, а не символов, т.к. символ кириллицы имеет длинну - 2 байта, латиницы - 1 байт
        Network.get().getOut().writeByte((byte) 3);
        System.out.println("Sent byte = 3");
        Network.get().getOut().writeInt(b.length);
        Network.get().getOut().write(fileName.getBytes(StandardCharsets.UTF_8));
        long size = Network.get().getIn().readLong();
        System.out.println("Downloading file length - " + size);
        File file = new File(clientDir + "/" + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[4];
        for (int i = 0; i < (size + 3) / 4; i++) {
            // последний передаваемый буфер необходимо закидывать побайтово, т.к. целый буфер почему-то не докидывается в сеть
            if (i == (size + 3) / 4 - 1) {
                // передача последнего целого буфера
                if (size % 4 == 0) {
                    for (int j = 0; j < 4; j++) {
                        fos.write(Network.get().getIn().readByte());
                    }
                    // передача последнего дробного буфера
                } else {
                    for (int j = 0; j < size % 4; j++) {
                        fos.write(Network.get().getIn().readByte());
                    }
                }
                // передача целых буферов, кроме последнего
            } else {
                int read = Network.get().getIn().read(buffer);
                fos.write(buffer, 0, read);
            }
            double persentDouble = ((double)i + 1) * 100L / (((double)size + (4 - size % 4)) / 4);
            String percent = new DecimalFormat("#0.0").format(persentDouble);
            System.out.println("Loading - " + percent + " %");
        }
        fos.close();
        System.out.println("File received");
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
        Network.get().getOut().writeByte((byte) 1);

//        Network.get().getOut().write("ls".getBytes(StandardCharsets.UTF_8)); // was "/list"

        //В случае получения int
//        int g = Network.get().getIn().readInt();
//        System.out.println(g);

        //Получение байта количества байт для числа количества файлов
        byte[] lengthNumberFiles = new byte[1];
        lengthNumberFiles[0] = Network.get().getIn().readByte();
        //Перевод байтов в строку
        String lengthNumberFilesStr = new String(lengthNumberFiles, "UTF-8");
        //Перевод строки в инт
        int lengthNumberFilesInt = Integer.parseInt(lengthNumberFilesStr);

        //Получение байтов числа количества файлов
        byte[] numberFiles = new byte[lengthNumberFilesInt];
        for (int i = 0; i < lengthNumberFilesInt; i++) {
            numberFiles[i] = Network.get().getIn().readByte();
        }
        String numberFilesStr = new String(numberFiles, "UTF-8");
        int numberFilesInt = Integer.parseInt(numberFilesStr);

        //Получение байтов названий файлов
        ArrayList<String> serverFiles = new ArrayList<>();
        for (int j = 0; j < numberFilesInt; j++) {
            //Получение байта количества байт для числа длины названия файла
            byte[] lengthOfLength = new byte[1];
            lengthOfLength[0] = Network.get().getIn().readByte();
            String lengthOfLengthStr = new String(lengthOfLength, "UTF-8");
            int lengthOfLengthInt = Integer.parseInt(lengthOfLengthStr);

            //Получение байтов числа длины названия файла
            byte[] userFileLength = new byte[lengthOfLengthInt];
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
