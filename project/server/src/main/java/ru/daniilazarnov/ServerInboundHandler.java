package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ServerInboundHandler extends ChannelInboundHandlerAdapter {

    private static final List<Channel> channels = new ArrayList<>();
    private static int clientId = 1;
    private String clientName;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected!" + ctx);
        channels.add(ctx.channel());
        clientName = "Client #" + clientId;
        clientId++;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        StringBuilder sb = new StringBuilder();

        if (sb.append((char)buf.readByte()).toString().equals("/")){
            byte[] arr = new byte[buf.readableBytes()];
            buf.readBytes(arr);
            ctx.fireChannelRead(arr);
            return;
        } else {
            while (buf.readableBytes() > 0){
                sb.append((char)buf.readByte());
            }
        }

        String str = sb.toString();
        String out = String.format("[%s]: %s", clientName, str);

        for (Channel ch : channels){
            ch.writeAndFlush(out);
        }

        System.out.println(out);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(String.format("Client [%s] disconnected", clientName));
        channels.remove(ctx.channel());
        ctx.close();
    }
}
