package chat.helpers;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileHelperTest {

    @Test
    void saveFile() throws IOException {
        Path file = Path.of("upload/test.txt");
        if(!Files.exists(file)) Files.createFile(file);

        String str = "тестовая строка\n" + new Date().getTime();

        try(ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes())) {
            FileHelper.saveFile(bais, file);
        }

        assertTrue(Files.exists(file));
        assertEquals(str, Files.readString(file));
    }
}