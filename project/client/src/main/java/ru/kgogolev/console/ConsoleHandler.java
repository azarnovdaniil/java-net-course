package ru.kgogolev.console;

import io.netty.buffer.ByteBuf;
import ru.kgogolev.FileSystem;
import ru.kgogolev.StringConstants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class ConsoleHandler {
    private FileSystem fileSystem;

    public ConsoleHandler(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public ByteBuf handleMessage() {
        String line = null;
        ByteBuf message = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                line = br.readLine();
                if (line.startsWith(StringConstants.VIEW_FILES)) {
                    fileSystem.walkFileTree(line.split(" ")[1]);

                } else if (line.startsWith(StringConstants.VIEW_FILES_DETAILED)) {
                    fileSystem.walkAllFileTree(line.split(" ")[2]);

                } else if (line.startsWith(StringConstants.UPLOAD)) {
                    message = fileSystem.sendFile(Path.of("D:", "K.Gogolev", "Documents", "storage", "1.jpg"));
                    break;
                } else {
                    System.out.println(line);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }
}
