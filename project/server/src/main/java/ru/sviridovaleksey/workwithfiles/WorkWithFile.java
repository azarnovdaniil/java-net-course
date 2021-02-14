package ru.sviridovaleksey.workwithfiles;

import ru.sviridovaleksey.newClientConnection.MessageForClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class WorkWithFile {

    private final MessageForClient messageForClient;

    public WorkWithFile(MessageForClient messageForClient){
        this.messageForClient = messageForClient;

    }

    public void createDefaultDirectory(String defaultAddress){
        if (Files.isDirectory(Path.of(defaultAddress))) {
        } else {
            processCreate(defaultAddress, true, "");
        }
    }


    public void createNewFile (String userName, String address) {

        if (Files.exists(Path.of(address))) {
            messageForClient.err("Файл уже существует");
        } else {
            processCreate(address, false, userName);
        }

    }

    public void createNewDirectory (String userName, String address) {

        if (Files.isDirectory(Path.of(address))) {
            messageForClient.err("Такая папка уже существует");
        } else {
            processCreate(address, true, userName);
        }
    }

    public void createFirsDirectory (String defaultAddress) {
        if (Files.isDirectory(Path.of(defaultAddress))) {
            System.out.println("Папка пользователя уже существует");
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
            messageForClient.err(e.getMessage() + "Не удалось создать " + fullAddress);
        }
    }

    public void deleteDirectory(String address) {

        File file = new File(address);

        if (!Files.isDirectory(Path.of(address))) {
            messageForClient.err("Такой папки не существует");
        } else {
            recursiveDeleteDirectory(file);
        }
    }

    public void deleteFile(String address) {

        File file = new File(address);

        if (!Files.exists(Path.of(address))) {
            messageForClient.err("Такого файла не существует");
        } else {
            recursiveDeleteDirectory(file);
        }
    }


    private void recursiveDeleteDirectory(File file){
        // до конца рекурсивного цикла
        if (!file.exists())
            return;

        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {

                recursiveDeleteDirectory(f);
            }
        }

        file.delete();
        messageForClient.successfulAction("Удаленный файл или папка: " + file.getName());
        System.out.println("Удаленный файл или папка: " + file.getAbsolutePath());
    }

    public synchronized void writeByteToFile (String way, byte[] data, long cell) {
        try {
            File file = new File(way);
            RandomAccessFile rafWrite = new RandomAccessFile(file, "rw");
            rafWrite.seek(cell);
            rafWrite.write(data);
            rafWrite.close();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage() + "Неудачная запись в файл");
        } catch (IOException e) {
            System.out.println(e.getMessage() + "Неудачная запись в файл");
            e.printStackTrace();
        }
    }


}


