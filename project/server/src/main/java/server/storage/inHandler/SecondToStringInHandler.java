package server.storage.inHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SecondToStringInHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("2. SecondToStringInHandler");
//        String str = (String) msg;
//        ctx.writeAndFlush(msg + "StringToByteBufHandler ");
    }

}
