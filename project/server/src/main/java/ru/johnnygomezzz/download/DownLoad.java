package ru.johnnygomezzz.download;

import ru.johnnygomezzz.FileMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class DownLoad {
    private static final String PATH = ("project/server/storage/");

    public static void download(java.lang.Object msg) throws IOException {
        FileMessage fm = (FileMessage) msg;
        Files.write(Paths.get(PATH, fm.getFileName()),
                fm.getData(), StandardOpenOption.CREATE);
        System.out.println("Файл " + fm.getFileName() + " загружен");
    }
}
