package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import ru.daniilazarnov.model.RequestData;
import ru.daniilazarnov.model.ResponseData;
import ru.daniilazarnov.utils.FileUtils;

import java.io.File;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = Logger.getLogger(ServerHandler.class);

    private static final String SERVER_STORAGE = "project" + File.separator
            + "server" + File.separator + "storage";

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        LOGGER.info("Client connected " + ctx.channel().remoteAddress().toString());

        ctx.fireChannelRegistered();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        LOGGER.info("Chanel Read");
        try {
            RequestData requestData = (RequestData) msg;
            ResponseData responseData = new ResponseData();
            LOGGER.info("RequestData received = " + requestData);
            byte cmd = requestData.getCommand();

            String strMsg = msg.toString();
            if (strMsg.startsWith("-test")) {
                String text = strMsg.replaceFirst("-test", "");
                String filePath = SERVER_STORAGE + File.separator + "test.txt";
                FileUtils.createFile(filePath);
                FileUtils.addTextToFile(filePath, text);
            }
            if (cmd == (byte) 1) {
                char separator = requestData.getSeparator();
                String[] filenameAndContent = requestData.getContent().split(String.valueOf(separator), 2);
                String filename = filenameAndContent[0];
                String content = filenameAndContent[1];
                String filePath = SERVER_STORAGE + File.separator + filename;
                FileUtils.createFile(filePath);
                FileUtils.addTextToFile(filePath, content);
                ChannelFuture future = ctx.writeAndFlush(responseData);
                future.addListener(ChannelFutureListener.CLOSE);
                LOGGER.info(requestData);
            }
        } finally {
//            ctx.close();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        LOGGER.info("Read Complete...");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("Client disconnected " + ctx.channel().remoteAddress().toString());
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("Error..." + cause.toString(), cause);
        ctx.close();
    }
}
