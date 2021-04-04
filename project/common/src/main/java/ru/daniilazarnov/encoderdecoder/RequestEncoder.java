package ru.daniilazarnov.encoderdecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;
import ru.daniilazarnov.model.RequestData;

import java.nio.charset.StandardCharsets;

public class RequestEncoder extends MessageToByteEncoder<RequestData> {
    private static final Logger LOGGER = Logger.getLogger(RequestEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          RequestData msg, ByteBuf out) {
        LOGGER.info("Request Encoder");
        out.writeByte(msg.getCommand());
        out.writeChar(msg.getSeparator());
        out.writeInt(msg.getLength());
        out.writeCharSequence(msg.getContent(), StandardCharsets.UTF_8);
    }

}
