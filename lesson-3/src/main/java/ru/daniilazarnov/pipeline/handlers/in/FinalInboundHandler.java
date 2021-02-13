package ru.daniilazarnov.pipeline.handlers.in;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FinalInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("final");
        byte[] arr = (byte[]) msg;
        Files.write(Paths.get("1.txt"), arr);
        System.out.println("Сообщение записано в файл");
        ctx.writeAndFlush("Java");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
