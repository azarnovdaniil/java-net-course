package ru.daniilazarnov;

import io.netty.channel.*;

/**
 * 25   * Handles a client-side channel.
 * 26
 */
public class MainClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MessagePacket msg = new MessagePacket();
        msg.setCommandToServer(commandToServer.CREATE);
        msg.setPathToFileName("dir/test1.txt");
        msg.setContentMessage("Hello! This message was saved to file.");
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
        ctx.close();
    }
}