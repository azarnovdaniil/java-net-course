package ru.daniilazarnov.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
    private static final Logger LOGGER = Logger.getLogger(FileUtils.class);

    public static void createDir(String dirPath) {
        Path path = Path.of(dirPath);
        File directory = path.toFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static boolean isFileExist(String filePath) {
        File f = new File(filePath);
        return f.exists() && !f.isDirectory();
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

    public static List<String> getFilesInDirectory(String dirPath) {
        Path path = Path.of(dirPath);
        try {
            return Files.list(path)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.error("IOException", e);
            throw new IllegalStateException(e);
        }
    }
}
