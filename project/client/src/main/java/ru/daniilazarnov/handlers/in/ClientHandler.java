package ru.daniilazarnov.handlers.in;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.datamodel.RequestData;
import ru.daniilazarnov.datamodel.ResponseData;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    public static final int TEST_V = 123;

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
        RequestData msg = new RequestData();
        msg.setId(TEST_V);
        msg.setStringValue(
                "test msg from client");
        ChannelFuture future = ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ResponseData inMsg = (ResponseData) msg;
        System.out.println(inMsg.getIntValue());
        ctx.close();
    }
}
