package clientserver.commands;

import clientserver.Command;
import io.netty.channel.ChannelHandlerContext;

public class CommandUnknown implements Command {

    @Override
    public void apply(ChannelHandlerContext ctx, String content, byte signal) {
        return;
    }
}
