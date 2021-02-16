import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyTest {

    @Test
    void TestListFiles() throws IOException {
        long busyVolume = Files.walk(Path.of("F:\\Разработки\\Java\\Geek\\10_Java\\Project\\client"))
                .map(Path::toFile)
                .map(File::length)
                .reduce((i1, i2) -> i1+i2).get();
        System.out.println(busyVolume);
    }

    @Test
    void TestCommands() {

    }

    @Test
    void TestReadLine() {

    }


}
