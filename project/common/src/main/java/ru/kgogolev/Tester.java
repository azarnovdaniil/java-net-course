package ru.kgogolev;

import java.io.IOException;
import java.nio.file.Path;

public class Tester {
    public static void main(String[] args) throws IOException {
        new FileSystem().sendFile(Path.of("D:", "K.Gogolev", "Documents", "storage", "text1.txt"));
    }
}
