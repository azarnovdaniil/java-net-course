package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class CommandHandler extends ChannelInboundHandlerAdapter {

    Commands command = new Commands();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] arr = (byte[]) msg;
        String str = new String(arr, StandardCharsets.UTF_8);
        if (str.equals("ls")){
            System.out.println("command: " + str);
            command.listFiles(ctx);
        } else if (str.startsWith("upload ")){
            System.out.println("command: " + str);
            command.uploadFile(ctx, str);  // /upload 123.txt hello world!
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
