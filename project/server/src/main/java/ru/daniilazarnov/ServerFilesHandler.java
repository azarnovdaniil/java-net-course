package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;

public class ServerFilesHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger(ServerFilesHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FileMessage) {
            FileMessage receivedFile = (FileMessage) msg;

            LOGGER.debug("File message from client was received: " + receivedFile.getPartNumber() + " / " + receivedFile.getPartsCount());

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
        LOGGER.error("SWW at files handler", cause);
        super.exceptionCaught(ctx, cause);
    }
}
