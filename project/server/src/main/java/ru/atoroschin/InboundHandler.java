package ru.atoroschin;

import ru.atoroschin.Commands;
import ru.atoroschin.FileLoaded;
import ru.atoroschin.FileWorker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class InboundHandler extends ChannelInboundHandlerAdapter {
    private Map<Integer, FileLoaded> uploadedFiles;
    private FileWorker fileWorker;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        String storageDir = "storage";
        String userName = "user_1";
        String baseDir = storageDir + File.separator + userName;
        System.out.println("Подключился клиент " + ctx.channel().remoteAddress().toString());
        int maxVolume = 1;
        uploadedFiles = new HashMap<>();
        fileWorker = new FileWorker(baseDir, baseDir, maxVolume);

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
        System.out.println("Отключился клиент " + ctx.channel().remoteAddress().toString());
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
