package clientserver.commands;

import clientserver.FileLoaded;
import clientserver.FileWorker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

/**
 [команда 1б][длина сообщения 4б][номер части 4б] prefix{[хэш 4б][длина имени 4б][имя][размер файла 8б]} [содержимое]
 если номер части =-1, значит она одна
 метод send
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
    public void response(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer, FileLoaded> uploadedFiles, byte signal) {
        String name = fileWorker.receiveFile(buf, uploadedFiles);
        if (!name.equals("")) {
            System.out.println("Сохранен файл " + name);
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, FileWorker fileWorker, Map<Integer, FileLoaded> uploadedFiles) {
    }
}