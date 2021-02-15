package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;


public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static final String user ="testUser";

    public ClientHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (ReceivingFiles.getCurrentState() == State.IDLE) {
            ByteBuf buf = ((ByteBuf) msg);
            byte readed = buf.readByte();
            Commands command = Commands.valueOf(readed);

            switch (command) {
                case DLF:
                    ReceivingFiles.fileReceive(msg, user);
                    break;

                case FLS:
                    String catalogStrings = ReceivingAndSendingStrings.receiveAndEncodeString(buf);
                    System.out.println(catalogStrings);
                    break;

                default:
                    System.err.println("(class ClientHandler) ERROR: Invalid first byte - " + readed);
            }
        }
        if (ReceivingFiles.getCurrentState() == State.FILE) {
            ReceivingFiles.fileReceive(msg, user);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
