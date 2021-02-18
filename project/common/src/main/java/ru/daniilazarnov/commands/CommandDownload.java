package ru.daniilazarnov.commands;

import ru.daniilazarnov.BufWorker;
import ru.daniilazarnov.FileLoaded;
import ru.daniilazarnov.FileWorker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Map;

public class CommandDownload implements Command {
    @Override
    public void send(ChannelHandlerContext ctx, String content, FileWorker fileWorker, byte signal) {
        fileWorker.sendCommandWithStringList(ctx, List.of(content), signal);
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles, byte signal) {
        String fileName = BufWorker.readFileListFromBuf(buf).get(0);
        byte[] bytes = fileWorker.makePrefixForFileSend(fileName);
        if (bytes.length > 0) {
            fileWorker.sendFile(ctx, fileName, signal, bytes);
            System.out.println("Download completed");
        } else {
            System.out.println("File not found");
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles) {
        String name = fileWorker.receiveFile(buf, uploadedFiles);
        if (!name.equals("")) {
            System.out.println("File safe " + name);
        }
    }
}