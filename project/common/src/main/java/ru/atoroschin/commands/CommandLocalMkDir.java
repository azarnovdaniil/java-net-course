package ru.atoroschin.commands;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.atoroschin.FileLoaded;
import ru.atoroschin.FileWorker;

import java.util.Map;

public class CommandLocalMkDir implements Command {
    @Override
    public void send(ChannelHandlerContext ctx, String content, FileWorker fileWorker, byte signal) {
        fileWorker.makeDir(content);
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker,
                         Map<Integer, FileLoaded> uploadedFiles, byte signal) {

    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker,
                        Map<Integer, FileLoaded> uploadedFiles) {

    }
}
