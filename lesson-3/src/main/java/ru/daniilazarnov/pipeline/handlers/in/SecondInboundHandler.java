package ru.daniilazarnov.pipeline.handlers.in;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;

public class SecondInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("second");
        byte[] arr = (byte[]) msg;
        for (int i = 0; i < 3; i++) {
            arr[i]++;
        }
        System.out.println("Второй шаг: " + Arrays.toString(arr));
        ctx.fireChannelRead(arr);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
