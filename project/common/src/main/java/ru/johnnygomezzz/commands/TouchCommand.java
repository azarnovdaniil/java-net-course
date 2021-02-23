package ru.johnnygomezzz.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TouchCommand {

    private static final String PATH_LOCAL = ("project/client/local/");
    private static final String PATH_STORAGE = ("project/server/storage/");

    private String fileName;
    private String content;
    private int length;

    public TouchCommand(String fileName, int length) {
        this.fileName = fileName;
        this.length = length;
    }

    public TouchCommand(String fileName, String content, int length) {
        this.fileName = fileName;
        this.content = content;
        this.length = length;
    }

    public void touchCommand() throws IOException {
        if (Files.exists(Path.of(PATH_LOCAL, fileName))) {
            System.out.println("Файл с именем " + fileName + " уже существует.");
        } else if (length == 2) {
            File file = new File(PATH_LOCAL, fileName);
            if (file.createNewFile()) {
                System.out.println("Пустой файл " + fileName + " успешно создан.");
            }
        } else {
            Path path = Paths.get(PATH_LOCAL, fileName);
            try {
                byte[] bs = content.getBytes();
                Path writtenFilePath = Files.write(path, bs);
                System.out.println("Файл " + fileName + " успешно создан.\nЗаписано в файл:\n"
                        + new String(Files.readAllBytes(writtenFilePath)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void touchCommandServer() throws IOException {
        if (Files.exists(Path.of(PATH_STORAGE, fileName))) {
            System.out.println("Файл с именем " + fileName + " уже существует на сервере.");
        } else if (length == 2) {
            File file = new File(PATH_STORAGE, fileName);
            if (file.createNewFile()) {
                System.out.println("Пустой файл " + fileName + " успешно создан на сервере.");
            }
        } else {
            Path path = Paths.get(PATH_STORAGE, fileName);
            try {
                byte[] bs = content.getBytes();
                Path writtenFilePath = Files.write(path, bs);
                System.out.println("Файл " + fileName + " успешно создан на сервере.\nЗаписано в файл:\n"
                        + new String(Files.readAllBytes(writtenFilePath)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
