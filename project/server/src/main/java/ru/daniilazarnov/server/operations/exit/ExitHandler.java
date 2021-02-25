package ru.daniilazarnov.server.operations.exit;

import io.netty.channel.Channel;
import ru.daniilazarnov.common.handlers.Handler;

public class ExitHandler implements Handler {

    private Channel channel;

    public ExitHandler(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void handle() {
        System.out.println("Client disconnected");
        channel.close();
    }

    @Override
    public boolean isComplete() {
        return true;
    }
}
