package ru.daniilazarnov.commands;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import ru.daniilazarnov.FileLoaded;
import ru.daniilazarnov.FileWorker;
import java.util.Map;

public class CommandUpload implements Command {

    @Override
    public void send(ChannelHandlerContext ctx, String content, FileWorker fileWorker, byte signal) {
        byte[] bytes = fileWorker.makePrefixForFileSend(content); // служебные данные в начале сообщения
        if (bytes.length > 0) {
            fileWorker.sendFile(ctx, content, signal, bytes);
            System.out.println("Выгрузка завершена");
        } else {
            System.out.println("Файл не найден");
        }
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles, byte signal) {
        String name = fileWorker.receiveFile(buf, uploadedFiles);
        if (!name.equals("")) {
            System.out.println("Сохранен файл " + name);
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles) {
    }
}