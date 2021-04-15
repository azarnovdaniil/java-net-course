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

public class FileEncoder {
    public static final byte MAGIC_BYTE = (byte) 25;

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
