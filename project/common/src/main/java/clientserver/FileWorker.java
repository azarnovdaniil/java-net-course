package clientserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class FileWorker {
    private static final int PART_SIZE = 10 * 1024 * 1024;
    private String currentDir;

    public static void sendFile(ChannelHandlerContext ctx, String content, byte signal, byte[] bytes) {
        try {
            Path file = Path.of(content);
            long volume = Files.size(file);
            long sendVolume = 0;
            int countPar = (int) volume / PART_SIZE + 1;

            int lenServiceData = 1 + 4 + 4 + bytes.length;
            byte[] buffer = new byte[PART_SIZE - lenServiceData];

            int partNum = countPar == 1 ? -1 : 1;
            int i = 0;
            FileInputStream fileInputStream = new FileInputStream(file.toFile());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream, PART_SIZE - lenServiceData);
            while (sendVolume < volume) {
                int readLength = bufferedInputStream.read(buffer);
                ByteBuf bufOut = ByteBufAllocator.DEFAULT.buffer(PART_SIZE);
                bufOut.writeByte(signal); // команда
                bufOut.writeInt(lenServiceData + readLength); // длина сообщения
                bufOut.writeInt(partNum + i); // 3 номер части
                bufOut.writeBytes(bytes); // служебные данные. все, кроме содержимого файла
                bufOut.writeBytes(buffer, 0, readLength); // содержимое файла

                ctx.writeAndFlush(bufOut);
                sendVolume += readLength;
                i++;
            }
            bufferedInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static byte[] makePrefixForFileSend(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) return new byte[]{(byte) 0};
        String name = file.getName();
        int hash = getHash(file);
        int lengthResponse;
        long volume = file.length();
        lengthResponse = 4 + 4 + name.getBytes(StandardCharsets.UTF_8).length + 8;
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(lengthResponse);

        buf.writeInt(hash); // 4 хэш
        buf.writeInt(name.getBytes(StandardCharsets.UTF_8).length); // 5 длина имени
        buf.writeBytes(name.getBytes(StandardCharsets.UTF_8)); // 6 имя
        buf.writeLong(volume);

        byte[] bufOut = new byte[lengthResponse];
        buf.readBytes(bufOut, 0, lengthResponse);
        return bufOut;
    }

    private static int getHash(File file) {
        return file.hashCode();
    }

    public static String receiveFile(ByteBuf buf, String currentDir, Map<Integer, FileLoaded> uploadedFiles) {
        buf.readInt(); // длина всего сообщения
        int partNum = buf.readInt();
        int hash = buf.readInt();
        int fileNameSize = buf.readInt();
        ByteBuf fileNameBuf = buf.readBytes(fileNameSize);
        String fileName = fileNameBuf.toString(StandardCharsets.UTF_8);
        fileNameBuf.release();
        long fileSize = buf.readLong();
        FileLoaded fileLoaded;
        Path path = Path.of(currentDir, fileName);
        if (uploadedFiles.containsKey(hash)) {
            fileLoaded = uploadedFiles.get(hash);
        } else {
            fileLoaded = new FileLoaded(hash, fileName, fileSize, path);
            uploadedFiles.put(hash, fileLoaded);
        }
        try {
            int writeSize = buf.readableBytes();
            byte[] bufIn = new byte[writeSize];

            buf.readBytes(bufIn);
            fileLoaded.addPart(partNum, writeSize, bufIn);

            if (fileLoaded.getSizeCounter() == fileSize) {
                uploadedFiles.remove(hash);
                return fileName;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return "";
    }
}
