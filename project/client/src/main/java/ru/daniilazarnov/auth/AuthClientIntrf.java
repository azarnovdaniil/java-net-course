package ru.daniilazarnov.auth;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface AuthClientIntrf {
    String getUserFolder();

    boolean isAuthStatus();

    void setAuthStatus(boolean authStatus);

    void authentication(ByteBuf buf, ChannelHandlerContext ctx);

    String getStringStatusAuth();

    String inputLoginAndPassword();
}
