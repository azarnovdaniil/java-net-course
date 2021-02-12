package clientserver.commands;

import clientserver.Commands;
import clientserver.FileLoaded;
import clientserver.FileWorker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import java.util.Map;

/**
 * [команда 1б][длина сообщения 4б][кол объектов 4 байта][длина имени1 4б][имя1]
 * клиент отправляет запрос на сервер
*/
public class CommandDownload implements Command {
    @Override
    public void send(ChannelHandlerContext ctx, String content, byte signal) {
        List<String> fileName = List.of(content);
        byte[] request = Commands.makeArrayFromList(fileName);
        ByteBuf bufOut = ctx.alloc().buffer(request.length);
        request[0] = signal;
        bufOut.writeBytes(request);
        ctx.writeAndFlush(bufOut);
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, String currentDir, Map<Integer, FileLoaded> uploadedFiles, byte signal) {
        //[команда 1б][длина сообщения 4б][номер части 4б] prefix{[хэш 4б][длина имени 4б][имя][размер файла 8б]} [содержимое]
        String fileName = Commands.readFileListFromBuf(buf).get(0);
        byte[] bytes = FileWorker.makePrefixForFileSend(fileName); // служебные данные в начале сообщения
        if (bytes.length > 0) {
            FileWorker.sendFile(ctx, fileName, signal, bytes);
            System.out.println("Выгрузка завершена");
        } else {
            System.out.println("Файл не найден");
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, String currentDir, Map<Integer, FileLoaded> uploadedFiles) {
        String name = FileWorker.receiveFile(buf, currentDir, uploadedFiles);
        if (!name.equals("")) {
            System.out.println("Сохранен файл " + name);
        }
    }
}
