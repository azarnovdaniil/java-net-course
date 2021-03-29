package exercise.handlers.in;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;

public class FirstInboundHandler extends ChannelInboundHandlerAdapter {

    public static final int MESSAGE_SIZE = 3;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("first");
        ByteBuf buf = (ByteBuf) msg; // кастуем наш msg т.к. 1-е сообщение это всегда байт-буфер
        // здесь написан набор правил, которые составляют логику нашего приложения
        if (buf.readableBytes() < MESSAGE_SIZE) {
            buf.release();
            ctx.writeAndFlush("hahahah");
        }
        byte[] data = new byte[MESSAGE_SIZE];
        buf.readBytes(data);
        buf.release(); // делает пометку, что мы его прочитали и можем что то снова в него записывать
        System.out.println(Arrays.toString(data));
        ctx.fireChannelRead(data);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
