package ru.daniilazarnov.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class TestClientHandler extends ChannelInboundHandlerAdapter {

    private static final byte TEXT = (byte) 1;
    private static final byte CMD_LS = (byte) 45;
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
                if (signal == CMD_LS){
                    state = ClientState.LS;

                    byte[] bytes = new byte[buf.readableBytes()];
                    buf.readBytes(bytes);
                    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    List<String> list = (List<String>) ois.readObject();
                    for (String s : list){
                        System.out.println(s);
                    }

                    state = ClientState.IDLE;
                }
            }
        }
        buf.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Connection to the server is lost");
        cause.printStackTrace();
        ctx.close();
    }
}
