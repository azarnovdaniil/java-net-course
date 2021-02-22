package common.Commands.commandsIinterface;

import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

public interface Receive extends Serializable {
    void receive(ChannelHandlerContext ctx);

}
