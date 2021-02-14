package ru.sviridovaleksey.workwithfile;

import io.netty.channel.Channel;
import ru.sviridovaleksey.Command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

public class WorkWithFileClient     {



    public void createDefaultDirectory(String defaultAddress){
        if (Files.isDirectory(Path.of(defaultAddress))) {
        } else {
            processCreate(defaultAddress, true);
        }
    }


    public  void writeByteToFile (String way, byte[] data, long cell) {
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
                if(isCreateDirectoryOrFile) {
                    Path path = Files.createDirectory(Path.of(fullAddress));
                    System.out.println("Папка для пользователя " + "" + " создана " + path.toAbsolutePath());

                } else {
                    Path path = Files.createFile(Path.of(fullAddress));
                    System.out.println("Файл для пользователя " + "" + " создан " + path.toAbsolutePath());

                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage() + "Не удалось создать " + fullAddress);
        }
    }

    public void sendFileInServer(File file, String userName, Channel channel) {
        try {
            long cell = 0;
            int step = 980000;
            int sendSize;
            boolean endWrite = false;
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(cell);
            int length = (int) raf.length();

            while (length != 0) {
                if (length > step) {sendSize = step; length = length - step;}
                else {sendSize = length; length = 0; endWrite = true;}
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

}