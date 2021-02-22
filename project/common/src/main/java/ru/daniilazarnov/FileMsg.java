package ru.daniilazarnov;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class FileMsg implements Serializable {
    private String nameFile;
    private final byte[] bytes;

    public FileMsg(String nameFile, byte[] bytes) {
        this.nameFile = nameFile;
        this.bytes = bytes;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public String getNameFile() {
        return nameFile;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public static boolean checkExistsFile(String path, FileMsg fileMsg) {
        return Files.exists(Path.of(path + fileMsg.getNameFile()));
    }

    public static boolean checkExistsFile(String path) {
        return Files.exists(Path.of(path));
    }

    public static String getFileName(String path) {
        File file = new File(path);
        return file.getName();
    }


}
