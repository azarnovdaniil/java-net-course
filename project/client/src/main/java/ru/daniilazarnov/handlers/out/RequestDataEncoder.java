package ru.daniilazarnov.handlers.out;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ru.daniilazarnov.datamodel.RequestData;

import java.nio.charset.Charset;

public class RequestDataEncoder extends MessageToByteEncoder<RequestData> {
    private final Charset charset = Charset.forName("UTF-8");

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          RequestData msg, ByteBuf out) throws Exception {

        out.writeInt(msg.getId());
        out.writeInt(msg.getStringValue().length());
        out.writeCharSequence(msg.getStringValue(), charset);
    }
}
