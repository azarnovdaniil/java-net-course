package ru.daniilazarnov.encoderdecoder;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import org.apache.log4j.Logger;
import ru.daniilazarnov.model.RequestData;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class RequestDecoder extends ReplayingDecoder<RequestData> {
    private static final Logger LOGGER = Logger.getLogger(RequestDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in, List<Object> out) {
        LOGGER.info("Request Decoder");

        byte cmd = in.readByte();
        char separator = in.readChar();
        int length = in.readInt();
        String content = in.readCharSequence(length, StandardCharsets.UTF_8).toString();
        RequestData data = new RequestData();
        data.setCommand(cmd);
        data.setSeparator(separator);
        data.setLength(length);
        data.setContent(content);
        out.add(data);
    }
}
