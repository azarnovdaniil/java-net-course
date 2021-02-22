package upload;

import ru.daniilazarnov.FileMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class UpLoad {
    private static final String WAY_SERVER = ("project/server/src/main/java/file/");

    public static void download(java.lang.Object msg) throws IOException {
        FileMessage fm = (FileMessage) msg;
        Files.write(Paths.get(WAY_SERVER, fm.getFileName()),
                fm.getData(), StandardOpenOption.CREATE);
        System.out.println("отправил " + fm.getFileName());
    }
}
