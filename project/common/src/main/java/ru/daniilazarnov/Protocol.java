package ru.daniilazarnov;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Protocol {
    public static final byte STOR = 1;
    public static final byte MKD = 2;
    public static final byte CD = 3;
    public static final byte RETR = 4;
    public static final byte USER = 5;
    public static final byte CONNECT = 6;
    public static final byte HELP = 7;

    private static final int DEFAULT_BUFFER_SIZE = 4;

    public static String getStringFromSocketChannel(SocketChannel socketChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();
        int fileNameLength = byteBuffer.getInt();
        byteBuffer = ByteBuffer.allocate(fileNameLength);
        StringBuilder sb = new StringBuilder();
        while (fileNameLength > 0) {
            socketChannel.read(byteBuffer);
            byteBuffer.flip();
            while (byteBuffer.hasRemaining() && fileNameLength > 0) {
                sb.append((char) byteBuffer.get());
                fileNameLength--;
            }
            byteBuffer.compact();
        }
        String fileName = sb.toString();
        return fileName;
    }
}
