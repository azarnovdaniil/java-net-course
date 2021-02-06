package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private Command currentState =  Command.FLS;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Server start");

        ByteBuf byteBuf = ((ByteBuf) msg);

        while (byteBuf.readableBytes() > 0){
            if(currentState == Command.FLS){
                byte readed = byteBuf.readByte();
                if (readed == (byte)2){

                }
            }

            if(currentState == Command.DLF){

            }

            if(currentState == Command.ULF){

            }
        }
        if (byteBuf.readableBytes() == 0){
            byteBuf.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
