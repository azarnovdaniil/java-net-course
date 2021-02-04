package ru.sviridovaleksey.workwithfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WorkWithFile {

    private final String defaultAddress = "project/server/Storage/";

    public WorkWithFile(){
        createDefaultDirectory();
    }


    public void createNewFile (String userName, String fileName, String cellDirectory) {
        String fullAddress = defaultAddress + userName + "/" + fileName;

        if (Files.exists(Path.of(fullAddress))) {
            System.out.println("Файл уже существует");
        } else {
            processCreate(fullAddress, false);
        }

    }

    public void createNewDirectory (String userName, String directoryName) {
        String fullAddress = defaultAddress + userName + "/" + directoryName;
        if (Files.isDirectory(Path.of(defaultAddress+userName+"/"+directoryName))) {
            System.out.println("Папка пользователя уже существует");
        } else {
            processCreate(fullAddress, true);
            System.out.println("Папка пользователя " + userName + "создана");
        }
    }

    public void createFirsDirectory (String userName) {
        String fullAddress = defaultAddress + userName;
        if (Files.isDirectory(Path.of(defaultAddress+userName))) {
            System.out.println("Такая директория уже существует");
        } else {
            processCreate(fullAddress, true);
        }
    }

    private void createDefaultDirectory(){
        if (Files.isDirectory(Path.of(defaultAddress))) {
        } else {
            processCreate(defaultAddress, true);
        }
    }

    private void processCreate(String fullAddress, Boolean isCreateDirectoryOrFile) {
        try {
            while (!Files.exists(Path.of(fullAddress))) {
                if(isCreateDirectoryOrFile) {
                Files.createDirectory(Path.of(fullAddress));
                } else { Files.createFile(Path.of(fullAddress)); }
                }
        } catch (IOException e) {
            System.out.println(e.getMessage() + "Не удалось создать " + fullAddress);
        }
    }

    public void deleteDirectory(String userName, String nameDirectory) {
        String fullAddress = (defaultAddress + userName + "/" + nameDirectory);
        File file = new File(defaultAddress + userName + "/" + nameDirectory);

        if (!Files.isDirectory(Path.of(fullAddress))) {
            System.out.println("Такой папки не существует");
        } else {
            recursiveDeleteDirectory(file);
        }
    }

    public void deleteFile(String userName, String nameDirectory) {
        String fullAddress = (defaultAddress + userName + "/" + nameDirectory);
        File file = new File(defaultAddress + userName + "/" + nameDirectory);

        if (!Files.exists(Path.of(fullAddress))) {
            System.out.println("Такого файла не существует");
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
        System.out.println("Удаленный файл или папка: " + file.getAbsolutePath());
    }
}


