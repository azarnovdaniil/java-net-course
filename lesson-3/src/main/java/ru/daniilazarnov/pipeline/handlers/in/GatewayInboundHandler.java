package ru.daniilazarnov.pipeline.handlers.in;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;

public class GatewayInboundHandler extends ChannelInboundHandlerAdapter {

    public static final int MAGIC_NUMBER = 66;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("gateway");
        int sum = 0;
        byte[] arr = (byte[]) msg;
        for (byte b : arr) {
            sum += b;
        }
        if (sum == MAGIC_NUMBER) {
            ctx.fireChannelRead(arr);
        } else {
            System.out.println("Сообщение сломано: " + Arrays.toString(arr));
            ctx.writeAndFlush("Битое сообщение\n");
        }
    }
}
