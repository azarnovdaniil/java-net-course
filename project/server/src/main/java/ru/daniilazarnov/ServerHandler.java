package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private Commands currentState =  Commands.FLS;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = ((ByteBuf) msg);

        while (byteBuf.readableBytes() > 0){
            if(currentState == Commands.FLS){
                byte readed = byteBuf.readByte();
                if (readed == (byte)2){

                }
            }

            if(currentState == Commands.DLF){

            }

            if(currentState == Commands.ULF){

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
