package server.storage.inHandler;

import clientserver.Commands;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;

public class InboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("1. First");
        ByteBuf buf = (ByteBuf) msg;

        if (buf.readableBytes() < 1) {
            buf.release();
            ctx.writeAndFlush("Неверная команда");  // отправка сообщения
        }
        String str = buf.toString(CharsetUtil.UTF_8);
        byte comm = buf.getByte(0);
        switch (comm) {
            case 1:  // команда LS
                System.out.println("LS");
                break;
            case 2:
                System.out.println("MKDIR");
                break;
            default:
                break;
        }
        ctx.writeAndFlush("Получена команда LS");
//        ctx.fireChannelRead(msg);
    }
}
