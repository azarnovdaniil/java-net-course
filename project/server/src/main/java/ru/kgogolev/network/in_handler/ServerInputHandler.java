package ru.kgogolev.network.in_handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import ru.kgogolev.FileDecoder;
import ru.kgogolev.StringConstants;

public class ServerInputHandler extends ChannelInboundHandlerAdapter {
    private FileDecoder fileDecoder;

    public ServerInputHandler(FileDecoder fileDecoder) {
        this.fileDecoder = fileDecoder;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
//        System.out.println(inBuffer.toString());
        String input = buf.toString(CharsetUtil.UTF_8);
        if (input.split(" ")[0].equals(StringConstants.DOWNLOAD)) {
            ctx.writeAndFlush(msg);
        } else {
            fileDecoder.decodeFile(buf);
        }
        if (buf.readableBytes() == 0) {
            buf.release();
            ctx.writeAndFlush(Unpooled.copiedBuffer("File recieved", CharsetUtil.UTF_8));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился");
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
