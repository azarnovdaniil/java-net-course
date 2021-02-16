package ru;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class HandlerCommand extends ChannelInboundHandlerAdapter {
    private ChannelHandlerContext ctx;



    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Клиент подключился: " + ctx );
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        System.out.println("Клиент отключился");
        ctx.close();
    }

}