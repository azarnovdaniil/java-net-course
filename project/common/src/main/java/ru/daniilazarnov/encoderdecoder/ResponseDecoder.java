package ru.daniilazarnov.encoderdecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import ru.daniilazarnov.model.ResponseData;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResponseDecoder extends ReplayingDecoder<ResponseData> {

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in, List<Object> out) {
        ResponseData data = new ResponseData();
        data.setLength(in.readInt());
        data.setResponseMessage(in.readCharSequence(in.readInt(), StandardCharsets.UTF_8).toString());
        out.add(data);
    }
}
