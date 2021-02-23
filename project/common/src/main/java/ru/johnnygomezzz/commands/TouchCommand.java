package ru.johnnygomezzz.commands;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TouchCommand {

    private static final String PATH_LOCAL = ("project/client/local/");

    public void touchCommand(String fileName, String content, int length) {
        if (Files.exists(Path.of(PATH_LOCAL, fileName))) {
            System.out.println("Файл с именем " + fileName + " уже существует.");
        } else if (length == 2) {
            System.out.println("Вы пытаетесь создать пустой файл.");
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
}
