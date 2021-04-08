package ru.daniilazarnov;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Collection;

public class TestServer {

    @Test
    void testPath() {
        File root = new File("C:\\storage\\");

        Collection<File> files = FileUtils.listFiles(root, null, true);

        for (File file : files) {
            if (file.getName().equals("test.txt")) {
                String absPath = file.getAbsolutePath();
                String parPath = file.getParent();

                System.out.printf("%s%n%s", absPath, parPath);
            }
        }
    }

    @Test
    void testRoot() {
        File root = new File("C:\\storage\\");
        Collection<File> files = FileUtils.listFilesAndDirs(root, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File f : files) {
            System.out.println(f.getAbsolutePath());
        }
    }
}
