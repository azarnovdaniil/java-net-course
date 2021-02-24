package ru.sviridovaleksey.workwithfiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkWithFile {

    private static final Logger LOGGER = Logger.getLogger(WorkWithFile.class.getName());

    public WorkWithFile(Handler fileHandler) {
        LOGGER.addHandler(fileHandler);
    }

    public void createDefaultDirectory(String defaultAddress) {
        if (Files.exists(Path.of(defaultAddress))) {
        return;
        } else {
            processCreate(defaultAddress, true, "");
        }
    }


    public String createNewFile(String userName, String address) {

        if (Files.exists(Path.of(address))) {
          return "Файл уже существует";
        } else {
            return processCreate(address, false, userName);
        }

    }

    public String createNewDirectory(String userName, String address) {

        if (Files.isDirectory(Path.of(address))) {
            return "Такая папка уже существует";
        } else {
            return processCreate(address, true, userName);
        }
    }

    public void createFirsDirectory(String defaultAddress) {

        if (Files.isDirectory(Path.of(defaultAddress))) {
            LOGGER.log(Level.INFO, "Папка пользователя уже существует");
        } else {
            processCreate(defaultAddress, true, "");
        }
    }


    private String processCreate(String fullAddress, Boolean isCreateDirectoryOrFile, String userName) {
        String message = "0";
        try {
            while (!Files.exists(Path.of(fullAddress))) {
                if (isCreateDirectoryOrFile) {
                Path path = Files.createDirectory(Path.of(fullAddress));
                LOGGER.log(Level.INFO, "Папка для пользователя " + userName + " создана " + path.toAbsolutePath());
                message = "Папка для пользователя " + userName + " создана "
                         + path.getFileName();
                } else {
                 Path path = Files.createFile(Path.of(fullAddress));
                 LOGGER.log(Level.INFO, "Файл для пользователя " + userName + " создан " + path.toAbsolutePath());
                 message = "Файл для пользователя " + userName + " создан "
                            + path.getFileName();
                        }
                }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            message = e.getMessage() + "Не удалось создать " + fullAddress;
        }
        return message;
    }

    public String deleteDirectory(String address) {

        File file = new File(address);

        if (!Files.isDirectory(Path.of(address))) {
            return "Такой папки не существует";
        } else {
           return recursiveDeleteDirectory(file);
        }
    }

    public String deleteFile(String address) {

        File file = new File(address);

        if (!Files.exists(Path.of(address))) {
            return "Такого файла не существует";
        } else {
           return recursiveDeleteDirectory(file);
        }
    }

    public boolean renameFile(String userName, String oldName, String newName) {
        File oldFile = new File(oldName);
        File newFile = new File(newName);

        if (oldFile.renameTo(newFile)) {
            LOGGER.log(Level.INFO, "Файл переименован для юзера " + userName);
            return true;
        } else {
            LOGGER.log(Level.INFO, "Не удалось переименовать файл");
            return false;
        }
    }


    private String recursiveDeleteDirectory(File file) {
        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                recursiveDeleteDirectory(f);
            }
        }

        file.delete();
        LOGGER.log(Level.INFO, "Удаленный файл или папка: " + file.getAbsolutePath());
        return "Удаленный файл или папка: " + file.getName();
    }

    public synchronized void writeByteToFile(String way, byte[] data, long cell) {
        try {
            File file = new File(way);
            RandomAccessFile rafWrite = new RandomAccessFile(file, "rw");
            rafWrite.seek(cell);
            rafWrite.write(data);
            rafWrite.close();

        } catch (FileNotFoundException e) {
            LOGGER.log(Level.INFO, e.getMessage() + "Неудачная запись в файл");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage() + "Неудачная запись в файл");
        }
    }


}


