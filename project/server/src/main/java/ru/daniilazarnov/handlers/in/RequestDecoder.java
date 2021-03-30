package ru.daniilazarnov.handlers.in;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import ru.daniilazarnov.datamodel.RequestDataFile;

import java.nio.charset.Charset;
import java.util.List;

public class RequestDecoder extends ReplayingDecoder<RequestDataFile> {

    private final Charset charset = Charset.forName("UTF-8");

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in, List<Object> out) throws Exception {

        RequestDataFile data = new RequestDataFile();
        data.setIntValue(in.readInt());
        int strLen = in.readInt();
        data.setStringValue(
                in.readCharSequence(strLen, charset).toString());
        out.add(data);
    }
}
