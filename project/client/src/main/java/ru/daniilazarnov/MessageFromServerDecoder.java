package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MessageFromServerDecoder  extends ReplayingDecoder<MessagePacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        MessagePacket data = new MessagePacket();
        final byte[] array = in.array();

        // data.setCommandToServer(in.readInt());
        out.add(data);
    }
}