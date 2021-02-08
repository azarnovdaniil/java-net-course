package ru.gb.putilin.cloudstorage.server.handlers.show;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.gb.putilin.cloudstorage.server.handlers.FileHandler;

public class ShowHandler implements FileHandler {

    private ChannelHandlerContext ctx;
    private ByteBuf buf;

    public ShowHandler(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void handle() {

    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public void setBuffer(ByteBuf buf) {
        this.buf = buf;
    }
}
