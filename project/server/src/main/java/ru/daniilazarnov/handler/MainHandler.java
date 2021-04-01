package ru.daniilazarnov.handler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.*;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainHandler extends ChannelInboundHandlerAdapter { // (1)

    public static final String SERVER_REPO = "D:\\serverStorage";
    CommandServer cmd = new CommandServer();
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client connected...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Client disconnected...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);
        String s = new String(data);
        String[] strings = s.split(" ");
        System.out.println(s);

        if (strings[0].equals("/help")){
        s = cmd.callHelpManual().toString();
        }
        else if (strings[0].equals("/show")){
            s = cmd.showFiles(Path.of(SERVER_REPO)).toString();
        }
        else if (strings[0].equals("/upload")) {
            if (strings.length == 2) {
                String address = SERVER_REPO + "\\" + strings[1];
                ctx.pipeline().addLast(new DataInputHandler(address));
            }else
                { s = "Wrong command!! See help for detailes /help";}
        }
        else if (strings[0].equals("/rename")){
            if (strings.length == 3){
                String address = SERVER_REPO + "\\" + strings[1];
                String addressNew = SERVER_REPO + "\\" + strings[2];
                cmd.renameFile(address, addressNew);
                s = "File was renamed";
                } else
            { s = "Wrong command!! See help for detailes /help";}
        }
        else if (strings[0].equals("/delete")){
            if (strings.length == 2){
                String address = SERVER_REPO + "\\" + strings[1];
                cmd.deleteFile(address);
                s = "File was deleted";
            } else
            { s = "Wrong command!! See help for detailes /help";}
        }
            byte[] arr = s.getBytes();
            ByteBuf buf = ctx.alloc().buffer(arr.length);
            buf.writeBytes(arr);
            ctx.writeAndFlush(buf);

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}