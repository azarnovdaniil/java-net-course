package ru.daniilazarnov.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import ru.daniilazarnov.FileMsg;

import java.io.File;
import java.io.FileOutputStream;

public class FilesHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger(FilesHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FileMsg) {
            FileMsg receivedFile = (FileMsg) msg;

            LOGGER.debug("Получен файл от клиента: " + receivedFile.getPartNumber() + " / " + receivedFile.getPartsCount());

            boolean append = true;
            if (receivedFile.getPartNumber() == 1) {
                append = false;
            }

            File newFile = new File("./project/server_dir/" + receivedFile.getLogin() + "/" + receivedFile.getFilename());
            FileOutputStream fos = new FileOutputStream(newFile, append);

            fos.write(receivedFile.getData());
            fos.close();

        } else ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Сбой при работе обработчика файлов", cause);
        super.exceptionCaught(ctx, cause);
    }
}