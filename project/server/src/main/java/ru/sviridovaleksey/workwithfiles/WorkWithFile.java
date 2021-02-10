package ru.sviridovaleksey.workwithfiles;

import ru.sviridovaleksey.newClientConnection.MessageForClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WorkWithFile {

    private final String defaultAddress = "project/server/Storage/";
    private MessageForClient messageForClient;

    public WorkWithFile(MessageForClient messageForClient){
        this.messageForClient = messageForClient;
        createDefaultDirectory();
    }


    public void createNewFile (String userName, String fileName, String cellDirectory) {
        String fullAddress = defaultAddress + userName + "/" + fileName;

        if (Files.exists(Path.of(fullAddress))) {
            messageForClient.errCreateFile("Файл уже существует");
        } else {
            processCreate(fullAddress, false, userName);
        }

    }

    public void createNewDirectory (String userName, String directoryName) {
        String fullAddress = defaultAddress + userName + "/" + directoryName;
        if (Files.isDirectory(Path.of(defaultAddress+userName+"/"+directoryName))) {
            messageForClient.errCreateFile("Такая папка уже существует");
        } else {
            processCreate(fullAddress, true, userName);
        }
    }

    public void createFirsDirectory (String userName) {
        String fullAddress = defaultAddress + userName;
        if (Files.isDirectory(Path.of(defaultAddress+userName))) {
            System.out.println("Папка пользователя уже существует");
        } else {
            processCreate(fullAddress, true, userName);
        }
    }

    private void createDefaultDirectory(){
        if (Files.isDirectory(Path.of(defaultAddress))) {
        } else {
            processCreate(defaultAddress, true, "");
        }
    }

    private void processCreate(String fullAddress, Boolean isCreateDirectoryOrFile, String userName) {
        try {
            while (!Files.exists(Path.of(fullAddress))) {
                if(isCreateDirectoryOrFile) {
                Path path = Files.createDirectory(Path.of(fullAddress));
                 System.out.println("Папка для пользователя " + userName + " создана " + path.toAbsolutePath());
                 messageForClient.successfulAction("Папка для пользователя " + userName + " создана " + path.getFileName());
                } else {
                    Path path = Files.createFile(Path.of(fullAddress));
                    System.out.println("Файл для пользователя " + userName + " создан " + path.toAbsolutePath());
                    messageForClient.successfulAction("Файл для пользователя " + userName + " создан " + path.getFileName());
                        }
                }
        } catch (IOException e) {
            messageForClient.errCreateFile(e.getMessage() + "Не удалось создать " + fullAddress);
        }
    }

    public void deleteDirectory(String userName, String nameDirectory) {
        String fullAddress = (defaultAddress + userName + "/" + nameDirectory);
        File file = new File(defaultAddress + userName + "/" + nameDirectory);

        if (!Files.isDirectory(Path.of(fullAddress))) {
            messageForClient.errCreateFile("Такой папки не существует");
        } else {
            recursiveDeleteDirectory(file);
        }
    }

    public void deleteFile(String userName, String nameDirectory) {
        String fullAddress = (defaultAddress + userName + "/" + nameDirectory);
        File file = new File(defaultAddress + userName + "/" + nameDirectory);

        if (!Files.exists(Path.of(fullAddress))) {
            messageForClient.errCreateFile("Такого файла не существует");
        } else {
            recursiveDeleteDirectory(file);
        }
    }


    private void recursiveDeleteDirectory(File file){
        // до конца рекурсивного цикла
        if (!file.exists())
            return;

        //если это папка, то идем внутрь этой папки и вызываем рекурсивное удаление всего, что там есть
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                // рекурсивный вызов
                recursiveDeleteDirectory(f);
            }
        }
        // вызываем метод delete() для удаления файлов и пустых(!) папок
        file.delete();
        messageForClient.successfulAction("Удаленный файл или папка: " + file.getName());
        System.out.println("Удаленный файл или папка: " + file.getAbsolutePath());
    }

}


