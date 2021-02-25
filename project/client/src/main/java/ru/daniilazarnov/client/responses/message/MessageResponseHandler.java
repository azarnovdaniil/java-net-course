package ru.daniilazarnov.client.responses.message;

import io.netty.buffer.ByteBuf;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.common.handlers.HandlerException;
import ru.daniilazarnov.common.messages.TextMessageReadState;
import ru.daniilazarnov.common.messages.TextMessageReader;

public class MessageResponseHandler implements Handler {

    private ByteBuf buf;

    private final TextMessageReader messageReader;

    public MessageResponseHandler(ByteBuf buf) {
        messageReader = new TextMessageReader();
        this.buf = buf;
    }

    @Override
    public void handle() throws HandlerException {
        messageReader.readMessage(buf);

    }

    @Override
    public boolean isComplete() {
        return messageReader.getState() == TextMessageReadState.COMPLETE;
    }
}
