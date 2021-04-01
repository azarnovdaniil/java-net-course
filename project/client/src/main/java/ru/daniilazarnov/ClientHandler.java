package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (ReceiveFile.getCurrentState() == State.IDLE) {
            ByteBuf buf = ((ByteBuf) msg);
            byte readed = buf.readByte();
            Commands commands = Commands.getCommand(readed);
            if (commands == Commands.DOWNLOAD) {
                ReceiveFile.fileReceive(msg, "user");
                Client.walk();
            } else System.out.println(commands.getExplain());
        }
        if (ReceiveFile.getCurrentState() == State.FILE) {
            ReceiveFile.fileReceive(msg, "user");
        }

    }
}
