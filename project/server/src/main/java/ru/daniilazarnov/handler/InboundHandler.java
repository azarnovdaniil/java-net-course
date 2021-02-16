package ru.daniilazarnov.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import ru.daniilazarnov.Commands;
import ru.daniilazarnov.FileLoaded;
import ru.daniilazarnov.FileWorker;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class InboundHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = Logger.getLogger(InboundHandler.class);
    private Map<Integer, FileLoaded> uploadedFiles;
    private FileWorker fileWorker;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        String storageDir = "storage";
        String userName = "user_1";
        System.out.println("Client connected "+ctx.channel().remoteAddress().toString());
        uploadedFiles = new HashMap<>();
        fileWorker = new FileWorker(storageDir + File.separator + userName,storageDir + File.separator + userName);
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;

        if (buf.readableBytes() > 0) {
            byte b = buf.readByte();
            Commands command = Commands.getCommand(b);
            command.receiveAndSend(ctx, buf, fileWorker, uploadedFiles);
        }
        buf.release();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected "+ctx.channel().remoteAddress().toString());
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause);
        ctx.close();
    }
}
