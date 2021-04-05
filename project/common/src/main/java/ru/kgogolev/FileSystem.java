package ru.kgogolev;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileSystem {
    public static final byte MAGIC_BYTE = (byte) 25;
    public static final double TO_KB = 1000;

    public void walkFileTree(String path) throws IOException {
        File rootDir = new File(path);

        List<File> dirs = new ArrayList<>();
        List<File> files = new ArrayList<>();
        for (File currentFile : rootDir.listFiles()) {
            if (currentFile.isDirectory()) {
                dirs.add(currentFile);
            }
            if (currentFile.isFile()) {
                files.add(currentFile);
            }
        }
        for (File currentDir : dirs) {
            System.out.println(String.format("subdir: %48s | %20s | %12s ",
                    currentDir.getName(),
                    new Date(currentDir.lastModified()),
                    currentDir.list() == null ? "is empty" : currentDir.list().length + " files"));

        }
        for (File currentFile : files) {
            System.out.println(String.format("file: %50s | %20s | %10.0fKB",
                    currentFile.getName(),
                    new Date(currentFile.lastModified()),
                    Math.ceil(currentFile.length() * 1.0 / TO_KB)));
        }
    }


    public ByteBuf sendFile(Path path) throws IOException {

        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(MAGIC_BYTE);
        byte[] filenameBytes = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
        buf.writeInt(filenameBytes.length);
        buf.writeBytes(filenameBytes);
        buf.writeLong(Files.size(path));
        File file = path.toFile();
        BufferedInputStream bufInpStr = new BufferedInputStream(new FileInputStream(file));
        while (bufInpStr.available() > 0) {
            buf.writeByte(bufInpStr.read());
        }
        return buf;
    }
}



