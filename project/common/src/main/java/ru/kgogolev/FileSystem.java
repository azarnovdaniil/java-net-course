package ru.kgogolev;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FileSystem {
    public static final double TO_KB = 1000;

    public void walkFileTree(String path) throws IOException {
        Path of = Path.of(path);
        File rootDir = of.toFile();


        List<File> dirs = new ArrayList<>();
        List<File> files = new ArrayList<>();
        for (File currentFile : Objects.requireNonNull(rootDir.listFiles())) {
            if (currentFile.isDirectory()) {
                dirs.add(currentFile);
            }
            if (currentFile.isFile()) {
                files.add(currentFile);
            }
        }
        for (File currentDir : dirs) {
            System.out.printf("subdir: %48s | %20s | %12s %n",
                    currentDir.getName(),
                    new Date(currentDir.lastModified()),
                    currentDir.list() == null ? "is empty"
                            : Objects.requireNonNull(currentDir.list()).length + " files");

        }
        for (File currentFile : files) {
            System.out.printf("file: %50s | %20s | %10.0fKB%n",
                    currentFile.getName(),
                    new Date(currentFile.lastModified()),
                    Math.ceil(currentFile.length() * 1.0 / TO_KB));
        }
    }


}



