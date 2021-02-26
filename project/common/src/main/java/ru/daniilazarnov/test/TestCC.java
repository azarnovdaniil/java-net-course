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

    public void command(String string) throws IOException {
        String[] cmd = string.split("\\s", 2);
        String part1 = cmd[0];
        String part2;

        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        switch (part1.toUpperCase()) {
            case "REG":
                part2 = cmd[1]; //login & password
                buf.writeByte((byte) 15);
                buf.writeBytes(Utils.convertToByteBuf(part2));
                channel.writeAndFlush(buf);
                break;
            case "AUTH":
                part2 = cmd[1]; //login & password
                buf.writeByte((byte) 10);
                buf.writeBytes(Utils.convertToByteBuf(part2));
                channel.writeAndFlush(buf);
                break;
            case "LS":
                buf.writeByte((byte) 45);
                channel.writeAndFlush(buf);
                break;
            case "RM":
                part2 = cmd[1]; //file name
                buf.writeByte((byte) 50);
                buf.writeBytes(Utils.convertToByteBuf(part2));
                channel.writeAndFlush(buf);
                break;
            case "UPL": {
                part2 = cmd[1]; //full path to file
                sendFile(Paths.get(part2), channel, future -> {
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
