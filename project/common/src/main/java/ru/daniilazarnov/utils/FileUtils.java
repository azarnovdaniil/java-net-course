package ru.daniilazarnov.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class FileUtils {
    private static final Logger LOGGER = Logger.getLogger(FileUtils.class);

    public static void createDir(String dirPath) {
        Path path = Path.of(dirPath);
        File directory = path.toFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static void createFile(String filePath) {
        Path path = Path.of(filePath);
        File f = path.toFile();
        f.getParentFile().mkdirs();
        try {
            f.createNewFile();
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
    }

    public static void addTextToFile(String filePath, String text) {
        try {
            FileWriter fw = new FileWriter(filePath, true);
            fw.write("\n" + text);
            fw.close();
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
    }
}
