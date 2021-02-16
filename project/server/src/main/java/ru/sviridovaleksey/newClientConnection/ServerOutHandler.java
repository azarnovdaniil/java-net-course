package ru.sviridovaleksey.newclientconnection;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import ru.sviridovaleksey.Command;

public class ServerOutHandler extends ChannelOutboundHandlerAdapter {



    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        if (msg instanceof Command) {
            ctx.writeAndFlush(msg);
        }

    }

}
