package ru.daniilazarnov.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestCC {
    private Channel channel;

    public TestCC(Channel channel) {
        this.channel = channel;
    }

    public void command(String[] input) throws IOException {
        String cmd = input[0].toUpperCase();
        String part1 = input[1];


        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        switch (cmd) {
            case  "REG":
                buf.writeByte((byte) 15);
                buf.writeBytes(Utils.convertToByteBuf(part1));
                channel.writeAndFlush(buf);
                break;
            case  "AUTH":
                buf.writeByte((byte) 10);
                buf.writeBytes(Utils.convertToByteBuf(part1));
                channel.writeAndFlush(buf);
                break;
            case "LS":
                buf.writeByte((byte) 45);
                channel.writeAndFlush(buf);
                break;
            case "UPL": {
                sendFile(Paths.get(part1), channel, future -> {
                    if (!future.isSuccess()) {
                        System.out.println("Fail to upload file. Please try again.");
                        future.cause().printStackTrace();
                    }
                    if (future.isSuccess()) {
                        System.out.println("File uploaded successfully");
                    }
                });
                break;
            }
            case "DOWN": {
                buf.writeByte((byte) 36);
                channel.writeAndFlush(buf);
                break;
            }
        }
    }

    public static void sendFile(Path path, Channel channel, ChannelFutureListener finishListener) throws IOException {

        FileRegion region = new DefaultFileRegion(path.toFile(), 0, Files.size(path));

        ByteBuf buf;

        buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte((byte)35);
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte((byte)25);
        channel.writeAndFlush(buf);

        byte[] filenameBytes = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
        buf = ByteBufAllocator.DEFAULT.directBuffer(Integer.SIZE / Byte.SIZE);
        buf.writeInt(filenameBytes.length);
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(filenameBytes.length);
        buf.writeBytes(filenameBytes);
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(Long.SIZE / Byte.SIZE);
        buf.writeLong(Files.size(path));
        channel.writeAndFlush(buf);

        ChannelFuture transferOperationFuture = channel.writeAndFlush(region);
        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }
    }
}
