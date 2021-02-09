import clientserver.Commands;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MyTest {

    @Test
    void TestListFiles() throws IOException {
        File file = new File("src");
        if (file.exists()) System.out.println(file.getAbsolutePath());

                Files.list(Path.of("../../storage"))
                .map(Path::toFile)
                .map(File::getName)
                .forEach(System.out::println);
    }

    @Test
    void TestCommands() {
        Arrays.stream(Commands.values()).forEach(System.out::println);
    }
}
