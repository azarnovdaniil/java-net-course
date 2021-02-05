package server.storage.inHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ToByteDecoder extends ByteToMessageDecoder {
    private int limit;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("декодирую");
        ByteBuf bufOut = channelHandlerContext.alloc().buffer(byteBuf.readableBytes());
        if (limit==0) {
            byte comm = byteBuf.readByte();
            limit = byteBuf.readInt();
            bufOut.writeByte(comm);
            bufOut.writeInt(limit);
        }
        bufOut.writeBytes(byteBuf);
        this.internalBuffer().writeBytes(bufOut);
        System.out.println("internal buffer "+ this.internalBuffer().readableBytes());

        if (this.actualReadableBytes()==limit) {
            ByteBuf bufOut2 = channelHandlerContext.alloc().buffer(this.actualReadableBytes());
            this.internalBuffer().readBytes(bufOut2);
            list.add(bufOut2);
        }
    }
}
