package tests;

import helpers.ServerFileHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

class ServerFileHelperTest {

    @org.junit.jupiter.api.Test
    void saveFile() {
    }

    @org.junit.jupiter.api.Test
    void getFile() {
    }

    @org.junit.jupiter.api.Test
    void listDirectories() throws IOException {
        ArrayList<Path> r = (ArrayList<Path>) ServerFileHelper.listDirectories("test");

        for (Path p: r) {
            System.out.println(p.getFileName());
        }
    }

    @org.junit.jupiter.api.Test
    void listFilesInDirectory() throws IOException {
        /*ArrayList<Path> r = (ArrayList<Path>) ServerFileHelper.listFilesInDirectory("test", "test2");

        for (Path p: r) {
            System.out.println(p.getFileName());
        }*/
    }

    @org.junit.jupiter.api.Test
    void createDirectory() throws IOException {
        //assertFalse(ServerFileHelper.createDirectory("test", "test2"));
    }
}