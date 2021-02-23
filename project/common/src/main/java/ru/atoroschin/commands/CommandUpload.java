package ru.atoroschin.commands;

import ru.atoroschin.BufWorker;
import ru.atoroschin.FileLoaded;
import ru.atoroschin.FileWorker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * [команда 1б][длина сообщения 4б][номер части 4б] prefix{[хэш 4б][длина имени 4б][имя][размер файла 8б]} [содержимое]
 * если номер части =-1, значит она одна
 * метод send
 */

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
        try {
            String name = fileWorker.receiveFile(buf, uploadedFiles);
            fileWorker.sendCommandWithStringList(ctx, List.of("Сохранен файл " + name), signal);
        } catch (IOException e) {
            fileWorker.sendCommandWithStringList(ctx, List.of("Ошибка загрузки файла"), signal);
            e.printStackTrace();
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles) {
        String answer = BufWorker.readFileListFromBuf(buf).get(0);
        System.out.println(answer);
    }
}
