package exercise.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/*
 * это handler, который обрабатывает входящие сообщения(и что то с ними делает)
 * на вход и на выход мы всегда передаём и принимаем буфер
 */
public class HelloServerHandler extends ChannelInboundHandlerAdapter {
    public static int index = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // так смотрим кол-во подключаемых клиентов
        System.out.println(index);
        index++;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);

        byte[] arr = "Hello".getBytes();
        ByteBuf buf = ctx.alloc().buffer(arr.length);
        buf.writeBytes(arr);
        // передаём буфер, предварительно что нибудь записав в него
        ctx.writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}