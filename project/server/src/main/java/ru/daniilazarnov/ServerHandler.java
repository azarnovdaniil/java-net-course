package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

import static ru.daniilazarnov.CommandList.*;


public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String fileName;
        // TODO add main features(upload and download file from server and upload to server via using commands (d, u)
        if (msg.equals(QUIT.getCommand())) {
            ctx.close(); //Выход
        } else if (msg.equals(DOWNLOAD.getCommand())) {     //Скачка данных
            System.out.println("DOWNLOADING...");
        } else if (msg.equals(UPLOAD.getCommand())) {       //Загрузка данных
            System.out.println("UPLOADING...");
        } else {
            ctx.writeAndFlush("Command not found. Try again.\n"); //
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(String.format("Connected [%s]:%s", new Date(), ctx));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(String.format("Disconnected [%s]:%s", new Date(), ctx));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
