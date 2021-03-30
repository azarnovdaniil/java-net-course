package ru.daniilazarnov.handlers.in;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import ru.daniilazarnov.datamodel.ResponseDataFile;

import java.util.List;

public class ResponseDataDecoder extends ReplayingDecoder<ResponseDataFile> {
    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in, List<Object> out) throws Exception {

        ResponseDataFile data = new ResponseDataFile();
        data.setIntValue(in.readInt());
        out.add(data);
    }
}
