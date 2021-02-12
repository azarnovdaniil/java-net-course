package clientserver.commands;

import clientserver.Commands;
import clientserver.FileLoaded;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
  [команда 1б][длина сообщения 4б][кол объектов 4 байта][длина имени1 4б][имя1][длина имени2 4б][имя2]...
 */

public class CommandLS implements Command {

    @Override
    public void send(ChannelHandlerContext ctx, String content, byte signal) {
        // клиент отправляет запрос на сервер
        ByteBuf byBuf = ByteBufAllocator.DEFAULT.buffer();
        byBuf.writeByte(signal);
        byBuf.writeInt(5);
        ctx.writeAndFlush(byBuf);
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, String currentDir, Map<Integer, FileLoaded> uploadedFiles, byte signal) {
        // сервер получает команду и отправляет клиенту ответ
        try {
            List<String> filesInDir = Files.list(Path.of(currentDir))
                    .map(Path::toFile)
                    .map(File::getName)
                    .collect(toList());
            byte[] answer = Commands.makeArrayFromList(filesInDir);
            // отправляем этот список
            ByteBuf bufOut = ctx.alloc().buffer(answer.length);
            answer[0] = (byte) signal;
            bufOut.writeBytes(answer);
            ctx.writeAndFlush(bufOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, String currentDir, Map<Integer, FileLoaded> uploadedFiles) {
        // клиент получает ответ от сервера
        System.out.println(Commands.readFileListFromBuf(buf));
    }

}
