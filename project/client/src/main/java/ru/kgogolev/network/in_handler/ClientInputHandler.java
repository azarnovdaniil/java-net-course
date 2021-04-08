package ru.kgogolev.network.in_handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.kgogolev.FileDecoder;

public class ClientInputHandler extends ChannelInboundHandlerAdapter {
    private FileDecoder fileDecoder;

    public ClientInputHandler(FileDecoder fileDecoder) {
        this.fileDecoder = fileDecoder;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buffer = (ByteBuf) msg;

        if (buffer.readByte() == fileDecoder.MAGIC_BYTE) {
            buffer.resetReaderIndex();
            fileDecoder.decodeFile(buffer);
        } else {
            while (buffer.readableBytes() > 0) {
                System.out.print((char) buffer.readByte());
                System.out.flush();
            }
        }

    }
}
