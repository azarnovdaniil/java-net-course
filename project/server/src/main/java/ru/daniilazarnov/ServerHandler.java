package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger log = Logger.getLogger(ServerHandler.class);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        log.info("[Server]: Client connected " + ctx.channel().remoteAddress().toString());

        ctx.fireChannelRegistered();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            log.info("[Server]: Message received = " + msg);
            ctx.writeAndFlush(msg);
        } finally {
            ReferenceCountUtil.release(msg);
            //ctx.close();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        log.info("[Server]: Read Complete...");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("[Server]: Client disconnected " + ctx.channel().remoteAddress().toString());
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("[Server]: Error..." + cause.toString(), cause);
        ctx.close();
    }
}
