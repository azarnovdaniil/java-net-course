package common;

import io.netty.channel.ChannelHandlerContext;

import java.nio.channels.Channel;

public abstract class OneToOneEncoder {
    protected abstract Object encode(ChannelHandlerContext channelhandlercontext, Channel channel, Object obj) throws Exception;
}
