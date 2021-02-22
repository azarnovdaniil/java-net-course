package common.Commands.commandsIinterface;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

public interface Send extends Serializable {
    void send(ChannelHandlerContext ctx);
}
