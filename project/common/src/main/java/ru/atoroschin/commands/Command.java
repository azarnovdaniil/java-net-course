package ru.atoroschin.commands;

import io.netty.buffer.ByteBufAllocator;
import ru.atoroschin.FileLoaded;
import ru.atoroschin.FileWorker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.Map;

public interface Command {
    void send(ChannelHandlerContext ctx, String content, FileWorker fileWorker, byte signal);

    void response(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles, byte signal);

    void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles) throws IOException;

    default void sendSimpleCommand(ChannelHandlerContext ctx, byte signal) {
        final int minLength = 5;
        ByteBuf byBuf = ByteBufAllocator.DEFAULT.buffer();
        byBuf.writeByte(signal);
        byBuf.writeInt(minLength);
        ctx.writeAndFlush(byBuf);
    }
}
