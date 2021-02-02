package ru.daniilazarnov.pipeline.handlers.in;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;

public class GatewayInboundHandler extends ChannelInboundHandlerAdapter {

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
        for (int i = 0; i < 3; i++) {
            sum += arr[i];
        }
        if (sum == 66) {
            ctx.fireChannelRead(arr);
        } else {
            System.out.println("Сообщение сломано: " + Arrays.toString(arr));
            ctx.writeAndFlush("Битое сообщение\n");
        }
    }
}
