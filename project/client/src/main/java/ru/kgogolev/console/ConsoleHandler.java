package ru.kgogolev.console;

import ru.kgogolev.FileSystem;
import ru.kgogolev.StringConstants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ConsoleHandler {
    private FileSystem fileSystem;

    public ConsoleHandler(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    public byte[] handleMessage() {
        String line = null;
        byte[] message = null ;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                line = br.readLine();
                if (line.startsWith(StringConstants.VIEW_FILES)) {
                    fileSystem.walkFileTree(line.split(" ")[1]);

                } else if (line.startsWith(StringConstants.VIEW_FILES_DETAILED)) {
                    fileSystem.walkAllFileTree(line.split(" ")[2]);

                } else if (line.startsWith(StringConstants.UPLOAD)) {
                    message = line.split(" ")[1].getBytes(StandardCharsets.UTF_8);
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
