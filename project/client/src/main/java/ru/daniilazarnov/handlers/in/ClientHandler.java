package ru.daniilazarnov.handlers.in;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.CommonDataAdapter;
import ru.daniilazarnov.datamodel.RequestDataFile;
import ru.daniilazarnov.datamodel.ResponseDataFile;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    public static final int TEST_VAL = 123;
    CommonDataAdapter cap = new CommonDataAdapter();

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {

        System.out.println(ctx.read().toString());
        RequestDataFile msg = new RequestDataFile();
        msg.setIntValue(TEST_VAL);
        msg.setStringValue(
                "test msg from client");
        ChannelFuture future = ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ResponseDataFile inMsg = (ResponseDataFile) msg;
        System.out.println(inMsg.getIntValue());
        ctx.close();
    }
}
