package ru.daniilazarnov.commands;

import ru.daniilazarnov.BufWorker;
import ru.daniilazarnov.FileLoaded;
import ru.daniilazarnov.FileWorker;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class CommandLS implements Command {

    @Override
    public void send(ChannelHandlerContext ctx, String content, FileWorker fileWorker, byte signal) {
        final int minLength = 5;
        ByteBuf byBuf = ByteBufAllocator.DEFAULT.buffer();
        byBuf.writeByte(signal);
        byBuf.writeInt(minLength);
        ctx.writeAndFlush(byBuf);
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles, byte signal) {
        try {
            List<String> filesInDir = fileWorker.getFileListInDir();
            fileWorker.sendCommandWithStringList(ctx, filesInDir, signal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles) {
        System.out.println(BufWorker.readFileListFromBuf(buf));
    }

}
