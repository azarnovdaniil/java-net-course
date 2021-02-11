package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.FileOutputStream;

public class ServerFilesHandler extends ChannelInboundHandlerAdapter {

    /**
     * Хэндлер для чтения файлов, которые были присланы клиентом
     */

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FileMessage) {
            FileMessage receivedFile = (FileMessage) msg;

            System.out.println(receivedFile.getPartNumber() + " / " + receivedFile.getPartsCount());

            boolean append = true;
            if (receivedFile.getPartNumber() == 1) {
                append = false;
            }

            File newFile = new File("./project/server_vault/" + receivedFile.getLogin() + "/" + receivedFile.getFilename());
            FileOutputStream fos = new FileOutputStream(newFile, append);

            fos.write(receivedFile.getData());
            fos.close();

        }
        else ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
