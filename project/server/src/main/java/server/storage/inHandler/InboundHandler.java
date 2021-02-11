package server.storage.inHandler;

import clientserver.Commands;
import clientserver.FileLoaded;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class InboundHandler extends ChannelInboundHandlerAdapter {
    private String currentDir;
    private Map<Integer, FileLoaded> uploadedFiles;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        String storageDir = "storage";
        String userName = "user_1";
        System.out.println("Подключился клиент "+ctx.channel().remoteAddress().toString());
        currentDir = storageDir + File.separator + userName;
        uploadedFiles = new HashMap<>();
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        if (buf.readableBytes() > 0) {
            byte b = buf.readByte();
            Commands command = Commands.getCommand(b);
            command.receiveAndSend(ctx, buf, currentDir, uploadedFiles);
        }
        buf.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
