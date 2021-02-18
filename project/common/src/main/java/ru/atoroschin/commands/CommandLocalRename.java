package ru.atoroschin.commands;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.atoroschin.FileLoaded;
import ru.atoroschin.FileWorker;

import java.util.Map;

public class CommandLocalRename implements Command {
    @Override
    public void send(ChannelHandlerContext ctx, String content, FileWorker fileWorker, byte signal) {
        String[] names = content.split("\\s");
        fileWorker.renameFile(names[0], names[1]);
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
