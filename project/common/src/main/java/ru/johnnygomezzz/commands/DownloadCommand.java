package ru.johnnygomezzz.commands;

import ru.johnnygomezzz.AbstractMessage;
import ru.johnnygomezzz.FileMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class DownloadCommand {
    private static final String PATH_LOCAL = ("project/client/local/");

    public void downloadCommand(AbstractMessage am) throws IOException {
        if (am instanceof FileMessage) {
            FileMessage fm = (FileMessage) am;
            Files.write(Paths.get(PATH_LOCAL, fm.getFileName()), fm.getData(), StandardOpenOption.CREATE);
            System.out.println("Файл " + fm.getFileName() + " успешно получен.");
        }
    }
}
