package ru.daniilazarnov.commands;

import ru.daniilazarnov.FileService.FileManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class UploadFile extends Commands {

    @Override
    public boolean runCommands(Path userDir, String homeDir, String fileName, byte[] content, int segment, int allSegments) {
        System.out.println("Загрузить файл\"" + fileName + "\" в директорию: " + userDir);

        if (!Files.exists(userDir)) {
            try {
                Files.createDirectories(userDir);

                System.out.println("Directory is created!");

            } catch (IOException e) {

                System.err.println("Failed to create directory!" + e.getMessage());

            }
        }
        Path newFilePath = null;
        try {
            newFilePath = Files.createFile(userDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("New file created: " + newFilePath);
        return false;
    }
}
