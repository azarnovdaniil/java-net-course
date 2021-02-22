package common.Commands.commandsIinterface;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

public interface Response extends Serializable {
    void response(ChannelHandlerContext ctx);
}
