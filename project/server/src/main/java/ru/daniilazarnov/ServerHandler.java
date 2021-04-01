package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.IOException;
import java.nio.file.Path;

import static ru.daniilazarnov.Commands.DOWNLOAD;


public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final String USER_FOLDER = "src/main/java/server/";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = ((ByteBuf) msg);
        if (ReceiveFile.getCurrentState() == State.IDLE) {
            byte readed = buf.readByte();
            Commands commands = Commands.getCommand(readed);
            if (commands == DOWNLOAD) {
                downloadFileFromServer(ctx, buf);
            }
        }
        if (ReceiveFile.getCurrentState() == State.FILE) {
            uploadFileToServer(msg);
        }
    }
    private void downloadFileFromServer(ChannelHandlerContext ctx, ByteBuf buf) throws IOException {
        String fileName = ReceiveString.receiveAndEncodeString(buf);
        System.out.println("fileName ".toUpperCase() + fileName);

        System.out.println("STATE: Start file download");
        FileSender.sendFile(Path.of(USER_FOLDER, fileName),
                ctx.channel(),
                ChannelFutureListen.getChannelFutureListener("File is successfully received"));
        buf.clear();
    }

    private void uploadFileToServer(Object msg) throws IOException {
        ReceiveFile.fileReceive(msg, "user");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected...");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
