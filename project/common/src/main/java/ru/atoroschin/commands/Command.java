package ru.atoroschin.commands;

import ru.atoroschin.FileLoaded;
import ru.atoroschin.FileWorker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

public interface Command {
    void send(ChannelHandlerContext ctx, String content, FileWorker fileWorker, byte signal);

    void response(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles, byte signal);

    void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles);
}