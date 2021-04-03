package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;
import ru.daniilazarnov.utils.FileUtils;

import java.io.File;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = Logger.getLogger(ServerHandler.class);

    private static final String SERVER_STORAGE = "project" + File.separator +
            "server" + File.separator + "storage";

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        LOGGER.info("Client connected " + ctx.channel().remoteAddress().toString());

        ctx.fireChannelRegistered();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            LOGGER.info("Message received = " + msg);
            String strMsg = msg.toString();
            if (strMsg.startsWith("-test")) {
                String text = strMsg.replaceFirst("-test", "");
                String filePath = SERVER_STORAGE + File.separator + "test.txt";
                FileUtils.createFile(filePath);
                FileUtils.addTextToFile(filePath, text);
            }
            if (strMsg.startsWith("-upload")) {
                String data = strMsg.replaceFirst("-test", "");
                String[] filenameAndContent = data.split("@", 2);
                String filename = filenameAndContent[0];
                String content = filenameAndContent[1];
                String filePath = SERVER_STORAGE + File.separator + filename;
                FileUtils.createFile(filePath);
                FileUtils.addTextToFile(filePath, content);
            }
            ctx.writeAndFlush(msg);
        } finally {
            //ctx.close();
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
