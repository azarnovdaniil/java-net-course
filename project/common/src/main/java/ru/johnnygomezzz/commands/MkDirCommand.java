package ru.johnnygomezzz.commands;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class MkDirCommand {
    private static final String PATH_LOCAL = ("project/client/local/");
    private static final String PATH_STORAGE = ("project/server/storage/");

    public void mkDirCommand(String dirName) {
        if (!Files.exists(Path.of(PATH_LOCAL, dirName))) {
            new File(PATH_LOCAL, dirName).mkdir();
            System.out.println("Каталог " + dirName + " успешно создан.");
        } else {
            System.out.println("Каталог " + dirName + " уже существует.");
        }
    }

    public void mkDirCommandServer(String dirName) {
        if (!Files.exists(Path.of(PATH_STORAGE, dirName))) {
            new File(PATH_STORAGE, dirName).mkdir();
            System.out.println("Каталог " + dirName + " успешно создан на сервере.");
        } else {
            System.out.println("Каталог " + dirName + " уже существует на сервере.");
        }
    }
}
