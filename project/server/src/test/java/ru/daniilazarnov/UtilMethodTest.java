package ru.daniilazarnov;

import org.junit.jupiter.api.Test;
import ru.daniilazarnov.util.UtilMethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class UtilMethodTest {
    @Test
    void createFolderTest() throws IOException {
        Path path = Paths.get("cloud_storage", "testUser");
        UtilMethod.createFolder("testUser");
        assertTrue(Files.exists(path));
        Files.delete(path);
    }

    @Test
    void testFile(){
        String HOME_FOLDER_PATH = Paths.get ("local_storage").toString();
        String fileName = "fileclient";
        Path path = Paths.get("project");
        System.out.println(path);
        System.out.println(Files.exists(path));
    }
}