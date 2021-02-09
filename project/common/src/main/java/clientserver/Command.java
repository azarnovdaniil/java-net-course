package clientserver;

import io.netty.channel.ChannelHandlerContext;

public interface Command {
    void apply(ChannelHandlerContext ctx, String content, byte signal);
}
