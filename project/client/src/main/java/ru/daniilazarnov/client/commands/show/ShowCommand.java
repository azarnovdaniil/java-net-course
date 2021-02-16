package ru.daniilazarnov.client.commands.show;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import ru.daniilazarnov.common.commands.Command;
import ru.daniilazarnov.common.OperationTypes;

import java.io.IOException;

public class ShowCommand implements Command {
    private Channel channel;

    public ShowCommand(Channel channel) {
        this.channel = channel;
    }


    private void sendShowCommand(ChannelFutureListener finishListener) throws IOException {
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(OperationTypes.SHOW.getCode());
        ChannelFuture transferOperationFuture = channel.writeAndFlush(buf);
        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }
    }

    @Override
    public void execute() {
        try {
            sendShowCommand(
                    future -> {
                        if (!future.isSuccess()) {
                            future.cause().printStackTrace();
                        }
                        if (future.isSuccess()) {
                            System.out.println("Files list request has been sent");
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
