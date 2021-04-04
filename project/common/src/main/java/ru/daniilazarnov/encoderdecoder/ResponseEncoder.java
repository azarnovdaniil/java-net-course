package ru.daniilazarnov.encoderdecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ru.daniilazarnov.model.ResponseData;

import java.nio.charset.StandardCharsets;

public class ResponseEncoder extends MessageToByteEncoder<ResponseData> {

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          ResponseData msg, ByteBuf out) {
        out.writeInt(msg.getLength());
        out.writeCharSequence(msg.getResponseMessage(), StandardCharsets.UTF_8);
    }
}
