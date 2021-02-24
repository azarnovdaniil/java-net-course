package ru.atoroschin.commands;

import ru.atoroschin.BufWorker;
import ru.atoroschin.FileLoaded;
import ru.atoroschin.FileWorker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * [команда 1б][длина сообщения 4б][номер части 4б] prefix{[хэш 4б][длина имени 4б][имя][размер файла 8б]} [содержимое]
 * если номер части =-1, значит она одна
 * метод send
 */

public class CommandUpload implements Command {
    private final Logger logger = Logger.getLogger(CommandUpload.class.getName());

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
            if (!name.equals("")) {
                String message = "Сохранен файл " + name;
                fileWorker.sendCommandWithStringList(ctx, List.of(message), signal);
                logger.info(message);
            }
        } catch (IOException e) {
            String message = "Ошибка загрузки файла";
            fileWorker.sendCommandWithStringList(ctx, List.of(message), signal);
            logger.log(Level.WARNING, message, e);
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer,
            FileLoaded> uploadedFiles) {
        String answer = BufWorker.readFileListFromBuf(buf).get(0);
        System.out.println(answer);
    }
}
