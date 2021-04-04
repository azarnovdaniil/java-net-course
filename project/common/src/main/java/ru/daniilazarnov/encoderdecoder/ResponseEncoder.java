package ru.daniilazarnov.encoderdecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;
import ru.daniilazarnov.model.ResponseData;

import java.nio.charset.StandardCharsets;

public class ResponseEncoder extends MessageToByteEncoder<ResponseData> {
    private static final Logger LOGGER = Logger.getLogger(ResponseEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          ResponseData msg, ByteBuf out) {
        LOGGER.info("Response Encoder");
        out.writeInt(msg.getLength());
        out.writeCharSequence(msg.getResponseMessage(), StandardCharsets.UTF_8);
    }
}
