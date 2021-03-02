package commands;

import ru.daniilazarnov.FileMessage;
import server.ServerHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class DownLoad extends ServerHandler {

    public static void download(java.lang.Object msg) throws IOException {
        FileMessage fm = (FileMessage) msg;
        Files.write(Paths.get(WAY_SERVER, fm.getAccount(), fm.getFileName()), fm.getData(), StandardOpenOption.CREATE);
        System.out.println("получен " + fm.getFileName());
    }
}
