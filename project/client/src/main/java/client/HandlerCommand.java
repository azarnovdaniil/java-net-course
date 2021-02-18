package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HandlerCommand extends ChannelInboundHandlerAdapter {

    private ChannelHandlerContext ctx;


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Готов принимать и отравлять команды");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msgObject) {

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
