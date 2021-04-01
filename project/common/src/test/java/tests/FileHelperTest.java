package tests;

import helpers.FileHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileHelperTest {

    @org.junit.jupiter.api.Test
    void saveFile() {
    }

    @org.junit.jupiter.api.Test
    void getFile() {
    }

    @org.junit.jupiter.api.Test
    void listDirectories() throws IOException {
        ArrayList<Path> r = (ArrayList<Path>) FileHelper.listDirectories("test");

        for (Path p: r) {
            System.out.println(p.getFileName());
        }
    }

    @org.junit.jupiter.api.Test
    void listFilesInDirectory() throws IOException {
        /*ArrayList<Path> r = (ArrayList<Path>) FileHelper.listFilesInDirectory("test", "test2");

        for (Path p: r) {
            System.out.println(p.getFileName());
        }*/
    }

    @org.junit.jupiter.api.Test
    void createDirectory() throws IOException {
        //assertFalse(FileHelper.createDirectory("test", "test2"));
    }
}