package ru.daniilazarnov.handlers.out;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ru.daniilazarnov.datamodel.ResponseDataFile;

public class ResponseDataEncoder extends MessageToByteEncoder<ResponseDataFile> {
    @Override
    protected void encode(ChannelHandlerContext ctx,
                          ResponseDataFile msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getIntValue());
    }
}
