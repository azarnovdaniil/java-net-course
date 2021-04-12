package ru.kgogolev.console;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import ru.kgogolev.FileSystem;
import ru.kgogolev.StringConstants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ConsoleHandler {
    private FileSystem fileSystem;
    private String currentDirectory;

    public ConsoleHandler(FileSystem fileSystem, String currentDirectory) {
        this.fileSystem = fileSystem;
        this.currentDirectory = currentDirectory;
    }

    public ByteBuf handleMessage() {
        String line = null;
        ByteBuf message = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                System.out.print(currentDirectory + " : ");
                line = br.readLine();
                if (line.startsWith(StringConstants.VIEW_FILES)) {
                    fileSystem.walkFileTree(currentDirectory);

                } else if (line.startsWith(StringConstants.UPLOAD)) {
                    String command = StringConstants.UPLOAD + " " + line.split(" ")[1];
                    message = Unpooled.wrappedBuffer(command.getBytes(StandardCharsets.UTF_8));
                    break;

                } else if (line.startsWith(StringConstants.AUTHENTIFICATION)) {
                    message = Unpooled.wrappedBuffer(line.getBytes(StandardCharsets.UTF_8));
                    break;
                } else if (line.startsWith(StringConstants.DOWNLOAD)) {
                    message = Unpooled.wrappedBuffer(line.getBytes(StandardCharsets.UTF_8));
                    break;
                } else {
                    System.out.println(StringConstants.UNKNOWN + " : " + line);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }
}
