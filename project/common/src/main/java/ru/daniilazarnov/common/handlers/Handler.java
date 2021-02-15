package ru.daniilazarnov.common.handlers;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public interface Handler {
    void handle() throws IOException;
    boolean isComplete();
    void setBuffer(ByteBuf buf);
}
