package ru.kgogolev;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class InputHandler extends ChannelInboundHandlerAdapter {
    //вызывается при подключении клиента
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился");
    }
//первый хендлер от сети принимает ByteBuf
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buffer = (ByteBuf) msg;
//        while (buffer.readableBytes()>0) {
//            System.out.print((char)buffer.readByte());
//            System.out.flush();
//        }
//        buffer.release();
//        ctx.writeAndFlush(buffer);
//        ctx.flush();
        ByteBuf inBuffer = (ByteBuf) msg;

        String received = inBuffer.toString(CharsetUtil.UTF_8);
        System.out.println("Server received: " + received);

        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello " + received, CharsetUtil.UTF_8));

    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
//                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
