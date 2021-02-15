package ru.daniilazarnov.commands;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import ru.daniilazarnov.FileLoaded;
import ru.daniilazarnov.FileWorker;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Map;

public class CommandUpload implements Command {

    private File serverFolder = new File(FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "Server");

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
}