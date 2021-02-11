package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ServerFilesHandler extends ChannelInboundHandlerAdapter {

    /**
     * Хэндлер для чтения файлов, которые были присланы клиентом
     */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FileMessage) {
            FileMessage receivedFile = (FileMessage) msg;

            Path newFile = Paths.get("./project/server_vault/" + receivedFile.getLogin() + "/" + receivedFile.getFilename());
            Files.write(
                    newFile,
                    receivedFile.getData(),
                    StandardOpenOption.CREATE);
        }
        else ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
