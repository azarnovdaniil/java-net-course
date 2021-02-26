package ru.daniilazarnov.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

public class TestClientHandler extends ChannelInboundHandlerAdapter {

    private ClientState state = ClientState.IDLE;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Connected to the server");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        while (buf.readableBytes() > 0){
            if (state == ClientState.IDLE){
                byte signal = buf.readByte();
                if (signal == Signals.LS.get()){
                    state = ClientState.LS;
                    List list = Utils.convertByteBufToList(buf);
                    for (Object s : list){
                        System.out.println(s);
                    }
                    state = ClientState.IDLE;
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Connection to the server is lost");
        cause.printStackTrace();
        ctx.close();
    }
}
