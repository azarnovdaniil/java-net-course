package client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.*;

public class ClientHandler {
    private static final int partSize = 1024*1024;

    public static boolean uploadFile(OutputStream out, String fileName, byte[] bytes) throws IOException {
        File file = new File(fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        long volume = file.length();
        int countPar = (int) volume / partSize + 1;
        byte[] buffer = new byte[partSize];
        int readLen = fileInputStream.read(buffer, 0, partSize);
        out.write(bytes);
        while (readLen == partSize) {
            out.write(buffer,0,readLen);
            readLen = fileInputStream.read(buffer, 0, partSize);
        }
        out.write(buffer,0,readLen);
        out.flush();

        return true;
    }
}
