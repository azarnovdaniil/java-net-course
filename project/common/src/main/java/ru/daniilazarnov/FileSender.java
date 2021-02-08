package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSender {
    public static void sendFile(Path path, Channel channel, ChannelFutureListener finishListener) throws IOException {
        FileRegion region = new DefaultFileRegion(new FileInputStream(path.toFile()).getChannel(), 0, Files.size(path));

        ByteBuf buf = null;
        buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte((byte) 25);
        channel.write(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(4);
        buf.writeInt(path.getFileName().toString().length());
        channel.write(buf);

        byte[] filenameBytes = path.getFileName().toString().getBytes();
        buf = ByteBufAllocator.DEFAULT.directBuffer(filenameBytes.length);
        buf.writeBytes(filenameBytes);
        channel.write(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(8);
        buf.writeLong(Files.size(path));
        channel.write(buf);

        ChannelFuture transferOperationFuture = channel.writeAndFlush(region);
        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }
    }
}