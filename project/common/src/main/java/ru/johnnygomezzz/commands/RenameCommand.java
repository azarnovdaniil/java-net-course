package ru.johnnygomezzz.commands;

import java.io.File;

public class RenameCommand {

    private static final String PATH_LOCAL = ("project/client/local/");
    private static final String PATH_STORAGE = ("project/server/storage/");


    public void renameCommand(String fileName, String fileNameTarget) {
        File file = new File(PATH_LOCAL, fileName);
        File newFile = new File(PATH_LOCAL, fileNameTarget);
        if (file.renameTo(newFile)) {
            System.out.println("Файл переименован успешно.");
        } else {
            System.out.println("Файл не переименован. Проверьте правильность написания.");
        }
    }

    public void renameCommandServer(String fileName, String fileNameTarget) {
        File file = new File(PATH_STORAGE, fileName);
        File newFile = new File(PATH_STORAGE, fileNameTarget);
        if (file.renameTo(newFile)) {
            System.out.println("Файл переименован успешно.");
        } else {
            System.out.println("Файл не переименован. Проверьте правильность написания.");
        }
    }
}
