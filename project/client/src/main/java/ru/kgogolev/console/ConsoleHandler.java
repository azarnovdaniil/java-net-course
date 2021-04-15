package ru.kgogolev.console;

import io.netty.buffer.ByteBuf;
import ru.kgogolev.FileSystem;
import ru.kgogolev.StringConstants;
import ru.kgogolev.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class ConsoleHandler {
    private final FileSystem fileSystem;
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

                } else if (line.startsWith(StringConstants.VIEW_SERVER_FILES)) {
                    message = StringUtil.lineToByteBuf(line);
                    break;
                } else if (line.startsWith(StringConstants.CHANGE_DIRECTORY)) {
                    currentDirectory = currentDirectory + File.separator + StringUtil.getWordFromLine(line, 1);


                } else if (line.startsWith(StringConstants.UPLOAD)) {
                    String command = StringConstants.UPLOAD + " " + line.split(" ")[1];
                    message = StringUtil.lineToByteBuf(line);
                    break;

                } else if (line.startsWith(StringConstants.AUTHENTIFICATION)) {
                    message = StringUtil.lineToByteBuf(line);
                    break;

                } else if (line.startsWith(StringConstants.DOWNLOAD)) {
                    message = StringUtil.lineToByteBuf(line);
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

    public void setCurrentDirectory(String currentDirectory) {
        this.currentDirectory = currentDirectory;
    }
}
