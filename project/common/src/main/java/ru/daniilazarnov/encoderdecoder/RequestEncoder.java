package ru.daniilazarnov.encoderdecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ru.daniilazarnov.model.RequestData;

import java.nio.charset.StandardCharsets;

public class RequestEncoder extends MessageToByteEncoder<RequestData> {

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          RequestData msg, ByteBuf out) throws Exception {

        out.writeByte(msg.getCommand());
        out.writeChar(msg.getSeparator());
        out.writeInt(msg.getLength());
        out.writeCharSequence(msg.getContent(), StandardCharsets.UTF_8);
    }

}
