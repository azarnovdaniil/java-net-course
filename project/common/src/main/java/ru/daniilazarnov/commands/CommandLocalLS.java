package ru.daniilazarnov.commands;

import ru.daniilazarnov.FileLoaded;
import ru.daniilazarnov.FileWorker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CommandLocalLS implements Command {
    @Override
    public void send(ChannelHandlerContext ctx, String content, FileWorker fileWorker, byte signal) {
        try {
            List<String> filesInDir = fileWorker.getFileListInDir();
            System.out.println(filesInDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles, byte signal) {

    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles) {

    }
}
