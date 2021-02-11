package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * Утилитный класс, содержит метод отправки файла
 */
public class FileSender {
    private static boolean loadingStatus = false; //переменная нужна для отправки статуса загрузки из других классов

    public static void setLoadingStatus(boolean loadingStatus) {
        FileSender.loadingStatus = loadingStatus;
    }

    public static boolean isLoadingStatus() {
        return loadingStatus;
    }


    /**
     * [] - 1b управляющий байт
     * [][][][] - 1 int длинна имени файла
     * [] - byte[?] имя файла
     * [][][][][][][][] long размер файла в байтах
     * [] data[] - содержимое файла
     *
     * @param path           - путь;
     * @param channel        - канал;
     * @param finishListener ;
     * @throws IOException;
     */
    public static void sendFile(Path path, Channel channel, ChannelFutureListener finishListener) throws IOException {
        FileRegion region = new DefaultFileRegion(new FileInputStream(path.toFile()).getChannel(), 0, Files.size(path));
        long totalTransferred = 0;


        ByteBuf buf;
        buf = ByteBufAllocator.DEFAULT.directBuffer(1);  //[] - 1b управляющий байт
        buf.writeByte((byte) 2);
        channel.write(buf);
        totalTransferred += 1;
        System.out.printf("Control byte transmitted: %d; total: %d%n;", 1, totalTransferred);

        buf = ByteBufAllocator.DEFAULT.directBuffer(4); //[][][][] - 1 int длинна имени файла
        buf.writeInt(path.getFileName().toString().length());
        channel.write(buf);
        totalTransferred += 4;
        System.out.printf("File length passed: %d; total: %d%n;", 4, totalTransferred);

        byte[] filenameBytes = path.getFileName().toString().getBytes();  //[] - byte[?] имя файла
        buf = ByteBufAllocator.DEFAULT.directBuffer(filenameBytes.length);
        buf.writeBytes(filenameBytes);
        channel.write(buf);
        totalTransferred += filenameBytes.length;
        System.out.printf("File name passed: %d; total: %d%n;", filenameBytes.length, totalTransferred);

        buf = ByteBufAllocator.DEFAULT.directBuffer(8); // [][][][][][][][] long размер файла в байтах
        buf.writeLong(Files.size(path));
        channel.write(buf);
        totalTransferred += 8;
        System.out.printf("File size transferred: %d; total: %d%n;", 8, totalTransferred);


        ChannelFuture transferOperationFuture = channel.writeAndFlush(region); //[] data[] - содержимое файла
        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }
        totalTransferred += Files.size(path);
        System.out.printf("File transfer complete: %d; total: %d%n;", Files.size(path), totalTransferred);
        buf.clear();
    }
}