package ru.daniilazarnov.commands;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ShowFile extends Commands {
    Path homeDirectory;

    @Override
    public boolean runCommands(Path userDir, String homeDir, String fileName, byte[] content, int segment, int allSegments) {

        if (!Files.exists(userDir)) {
            try {
                Files.createDirectories(userDir);
                System.out.println("Directory is created!");
            } catch (IOException e) {
                System.err.println("Failed to create directory!" + e.getMessage());
            }
        }
        if (!Files.exists(userDir.resolve(homeDir))) {
            try {
                homeDirectory = Files.createDirectories(userDir.resolve(homeDir));
                System.out.println("Directory is created!");
            } catch (IOException e) {
                System.err.println("Failed to create directory!" + e.getMessage());
            }
        }

        Path path = userDir;
        List<Path> paths = null;
        try {
            paths = listFiles(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        paths.forEach(x -> System.out.println(x));
        return true;
    }

    public static List<Path> listFiles(Path path) throws IOException {

        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }

        return result;
    }

}