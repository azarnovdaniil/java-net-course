package client;

import clientserver.Commands;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.*;
import java.util.Arrays;

public class ClientHandler {
    private static final int partSize = 1024*1024;

    public static boolean uploadFile(OutputStream out, String fileName, byte[] bytes) throws IOException {
        File file = new File(fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        long volume = file.length();
        int countPar = (int) volume / partSize + 1;

        int lenServiceData = 1+4+bytes.length;
        byte[] buffer = new byte[partSize];

        int readLen = fileInputStream.read(buffer, 0, partSize-lenServiceData);

//        lenServiceData += readLen;

        ByteBuf bufOut = ByteBufAllocator.DEFAULT.buffer(partSize);
        bufOut.writeByte(Commands.UPLOAD.getSignal()); // команда
        bufOut.writeInt(lenServiceData+readLen); // длина сообщения
        bufOut.writeBytes(bytes); // служебные данные. все, кроме содержимого файла
        bufOut.writeBytes(buffer, 0, partSize-lenServiceData); // содержимое файла
//        System.arraycopy(bytes, 0, buffer, 0, bytes.length);

        Arrays.fill(buffer,(byte) 0);
        bufOut.readBytes(buffer, 0, partSize-lenServiceData);


        out.write(buffer,0, readLen+lenServiceData);
        out.flush();
        System.out.println("отправлено байт: "+(readLen+lenServiceData));
//        volume -=readLen;
//        while (volume>0) {
//            readLen = fileInputStream.read(buffer, 0, partSize);
//            out.write(buffer,0,readLen);
//            out.flush();
//        }

        return true;
    }
}
