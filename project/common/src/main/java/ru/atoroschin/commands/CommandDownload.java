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
 * клиент отправляет запрос на сервер
 * [команда 1б][длина сообщения 4б][кол объектов 4 байта][длина имени1 4б][имя1]
 *
 * сервер отправляет файл
 * [команда 1б][длина сообщения 4б][номер части 4б] prefix{[хэш 4б][длина имени 4б][имя][размер файла 8б]} [содержимое]
*/
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
            System.out.println("Выгрузка завершена");
        } else {
            System.out.println("Файл не найден");
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles) throws IOException {
        String name = fileWorker.receiveFile(buf, uploadedFiles);
        if (!name.equals("")) {
            System.out.println("Сохранен файл " + name);
        }
    }
}
