package ru.johnnygomezzz.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DeleteCommand {
    private static final String PATH_LOCAL = ("project/client/local/");

    public void deleteCommand(String fileName) {
        if (Files.exists(Path.of(PATH_LOCAL, fileName))) {
            try {
                Files.delete(Paths.get(PATH_LOCAL, fileName));
                System.out.println("Файл с именем " + fileName + " успешно удалён.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Файл с именем " + fileName + " отсутствует.");
        }
    }
}
