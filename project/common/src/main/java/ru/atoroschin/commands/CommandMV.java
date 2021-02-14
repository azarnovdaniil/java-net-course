package ru.atoroschin.commands;

import ru.atoroschin.BufWorker;
import ru.atoroschin.FileLoaded;
import ru.atoroschin.FileWorker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import java.util.Map;

public class CommandMV implements Command {
    @Override
    public void send(ChannelHandlerContext ctx, String content, FileWorker fileWorker, byte signal) {
        String[] names = content.split("\\s");
        if (names.length == 2) {
            List<String> list = List.of(names);
            fileWorker.sendCommandWithStringList(ctx, list, signal);
        } else {
            System.out.println("Некорректный ввод данных");
        }
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles, byte signal) {
        List<String> list = BufWorker.readFileListFromBuf(buf);
        String fileName = list.get(0);
        String dirName = list.get(1);
        fileWorker.moveFile(fileName, dirName);
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles) {

    }
}
