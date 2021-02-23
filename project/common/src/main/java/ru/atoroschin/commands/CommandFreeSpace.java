package ru.atoroschin.commands;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.atoroschin.BufWorker;
import ru.atoroschin.FileLoaded;
import ru.atoroschin.FileWorker;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CommandFreeSpace implements Command {
    @Override
    public void send(ChannelHandlerContext ctx, String content, FileWorker fileWorker, byte signal) {
        sendSimpleCommand(ctx, signal);
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker,
                         Map<Integer, FileLoaded> uploadedFiles, byte signal) {
        long freeSpace = fileWorker.getFreeSpace();
        fileWorker.sendCommandWithStringList(ctx, List.of("Свободно на диске: " + freeSpace + " байт"), signal);
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker,
                        Map<Integer, FileLoaded> uploadedFiles) throws IOException {
        String answer = BufWorker.readFileListFromBuf(buf).get(0);
        System.out.println(answer);
    }
}
