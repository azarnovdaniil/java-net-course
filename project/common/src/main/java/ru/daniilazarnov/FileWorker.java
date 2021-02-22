package ru.daniilazarnov;

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
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class FileWorker {
    private static final int PART_SIZE = 10 * 1024 * 1024;
    private Path currentPath;
    private Path basePath;
    private String currentDir;

    public FileWorker(String currentPath, String basePath) {
        this.currentPath = Path.of(currentPath);
        this.basePath = Path.of(basePath);
        this.currentDir = this.currentPath.toAbsolutePath().toString();
        try {
            if (!Files.exists(this.currentPath)) {
                Files.createDirectory(this.currentPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCurrentDir() {
        return currentDir;
    }

    public void sendFile(ChannelHandlerContext ctx, String content, byte signal, byte[] bytes) {
        try {
            Path file = Path.of(currentDir, content);
            if (Files.exists(file)) {
                long volume = Files.size(file);
                long sendVolume = 0;
                int countPar = (int) volume / PART_SIZE + 1;

                final int countFour = 4;
                int lenServiceData = 1 + countFour + countFour + bytes.length;
                int lengthReadData = PART_SIZE - lenServiceData;
                byte[] buffer = new byte[lengthReadData];

                int partNum = countPar == 1 ? -1 : 1;
                int i = 0;
                FileInputStream fileInputStream = new FileInputStream(file.toFile());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream, lengthReadData);
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] makePrefixForFileSend(String fileName) {
        File file = Path.of(currentDir, fileName).toFile();
        if (!file.exists()) {
            return new byte[]{(byte) 0};
        }
        String name = file.getName();
        int hash = getHash(file);
        int lengthResponse;
        long volume = file.length();
        final int countFour = 4;
        final int countEight = 8;
        lengthResponse = countFour + countFour + name.getBytes(StandardCharsets.UTF_8).length + countEight;
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(lengthResponse);

        buf.writeInt(hash);
        buf.writeInt(name.getBytes(StandardCharsets.UTF_8).length);
        buf.writeBytes(name.getBytes(StandardCharsets.UTF_8));
        buf.writeLong(volume);

        byte[] bufOut = new byte[lengthResponse];
        buf.readBytes(bufOut, 0, lengthResponse);
        return bufOut;
    }

    private int getHash(File file) {
        return file.hashCode();
    }

    public String receiveFile(ByteBuf buf, Map<Integer, FileLoaded> uploadedFiles) {
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

    public void changeCurrentDir(String newDir) {
        if (Files.exists(currentPath.resolve(newDir))) {
            try {
                if (basePath.toFile().exists()) {
                    if (currentPath.resolve(newDir).toRealPath().startsWith(basePath.toRealPath())) {
                        currentPath = currentPath.resolve(newDir).toRealPath();
                    }
                } else {
                    currentPath = currentPath.resolve(newDir).toRealPath();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            currentDir = currentPath.toAbsolutePath().toString();
        }
    }

    public void sendCommandWithStringList(ChannelHandlerContext ctx, List<String> list, byte signal) {
        byte[] request = BufWorker.makeArrayFromList(list);
        ByteBuf bufOut = ctx.alloc().buffer(request.length);
        request[0] = signal;
        bufOut.writeBytes(request);
        ctx.writeAndFlush(bufOut);
    }
}
