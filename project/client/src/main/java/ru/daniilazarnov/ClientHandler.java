package client;

import common.Commands;
import common.ReceivingFile;
import common.State;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (ReceivingFile.getCurrentState() == State.IDLE) {
            ByteBuf buf = ((ByteBuf) msg);
            byte readed = buf.readByte();
            Commands commands = Commands.valueOf(readed);
            switch (commands) {
                case DOWNLOAD:
                    ReceivingFile.fileReceive(msg, "user");
                    Client.walk(); // вывод строки приглашения к вводу
                    break;
                case HELP:
                    System.out.println(commands.getHelp());
            }
        }
        if (ReceivingFile.getCurrentState() == State.FILE) {
            ReceivingFile.fileReceive(msg, "user");
        }

    }
}
