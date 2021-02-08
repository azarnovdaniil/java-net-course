package ru.gb.putilin.cloudstorage.server.handlers;

import io.netty.buffer.ByteBuf;

public interface FileHandler {
    void handle();
    boolean isComplete();
    void setBuffer(ByteBuf buf);
}
