package clientserver.commands;

import clientserver.Commands;
import clientserver.FileLoaded;
import clientserver.FilePartLoaded;
import clientserver.FileWorker;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

/**
 [команда 1б][длина сообщения 4б][номер части 4б] prefix{[хэш 4б][длина имени 4б][имя][размер файла 8б]} [содержимое]
 если номер части =-1, значит она одна
 метод send
*/

public class CommandUpload implements Command {
    public static int getPartSize() {
        return PART_SIZE;
    }

    private static final int PART_SIZE = 10 * 1024 * 1024;

    @Override
    public void send(ChannelHandlerContext ctx, String content, byte signal) {
        byte[] bytes = FileWorker.makePrefixForFileSend(content); // служебные данные в начале сообщения
        if (bytes.length > 0) {
            FileWorker.sendFile(ctx, content, signal, bytes);
            System.out.println("Выгрузка завершена");
        } else {
            System.out.println("Файл не найден");
        }
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, String currentDir, Map<Integer, FileLoaded> uploadedFiles, byte signal) {
        String name = FileWorker.receiveFile(buf, currentDir, uploadedFiles);
        if (!name.equals("")) {
            System.out.println("Сохранен файл " + name);
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, String currentDir, Map<Integer, FileLoaded> uploadedFiles) {
    }


}