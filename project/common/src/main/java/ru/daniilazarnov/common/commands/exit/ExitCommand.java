package ru.daniilazarnov.common.commands.exit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import ru.daniilazarnov.common.commands.Command;
import ru.daniilazarnov.common.commands.Commands;

public class ExitCommand implements Command {

    private Channel channel;

    public ExitCommand(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void execute() {
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(Commands.EXIT.getCode());
        channel.writeAndFlush(buf);
        System.out.println("Client disconnected");
        try {
            channel.closeFuture().addListener(ChannelFutureListener.CLOSE).sync();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }


}
