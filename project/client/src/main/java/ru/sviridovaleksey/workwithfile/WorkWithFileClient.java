package ru.sviridovaleksey.workwithfile;

import io.netty.channel.Channel;
import ru.sviridovaleksey.Command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class WorkWithFileClient     {



    public void createDefaultDirectory(String defaultAddress) {
        if (Files.isDirectory(Path.of(defaultAddress))) {
            return;
        } else {
            processCreate(defaultAddress, true);
        }
    }


    public  void writeByteToFile(String way, byte[] data, long cell) {
    try {
        System.out.println("##### идет скачивание ожидайте сообщения о завершении #####");
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


    private void processCreate(String fullAddress, Boolean isCreateDirectoryOrFile) {
        try {
            while (!Files.exists(Path.of(fullAddress))) {
                if (isCreateDirectoryOrFile) {
                    Path path = Files.createDirectory(Path.of(fullAddress));
                    System.out.println("Папка для пользователя " + "" + " создана " + path.toAbsolutePath());

                } else {
                    Path path = Files.createFile(Path.of(fullAddress));
                    System.out.println("Файл для пользователя " + "" + " создан " + path.toAbsolutePath());

                }
            }
        } catch (IOException e) {
            System.out.println("Не удалось создать " + fullAddress);
        }
    }

    public void sendFileInServer(File file, String userName, Channel channel) {
        try {
            long cell = 0;
            final int step = 980000;
            int sendSize;
            boolean endWrite = false;
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(cell);
            int length = (int) raf.length();

            while (length != 0) {
                if (length > step) {
                    sendSize = step; length = length - step;
                } else {
                    sendSize = length; length = 0; endWrite = true;
                }
                byte[] bt = new byte[sendSize];
                raf.read(bt);
                Command command = Command.writeInToFile(userName, file.getName(), bt, cell, endWrite);
                cell = cell + sendSize;
                channel.write(command);
            }
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(String address) {

        File file = new File(address);

        if ((!Files.exists(Path.of(address))) | (!Files.isDirectory(Path.of(address)))) {
            System.out.println("Такого файла не существует");
        } else {
            recursiveDeleteDirectory(file);
        }
    }

    private void recursiveDeleteDirectory(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                recursiveDeleteDirectory(f);
            }
        }

        file.delete();
        System.out.println("Удаленный файл или папка: " + file.getAbsolutePath());
    }

    public void renameFile(String oldName, String newName) {
        File oldFile = new File(oldName);
        File newFile = new File(newName);
        if (oldFile.renameTo(newFile)) {
            System.out.println("Файл " + oldName + " переименован в " + newName);
        } else {
            System.out.println("Не удалось переименовать файл");
        }
    }

}
