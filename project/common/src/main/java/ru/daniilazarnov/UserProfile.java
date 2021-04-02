package ru.daniilazarnov;

import io.netty.channel.socket.SocketChannel;

import java.util.function.BiConsumer;

public class UserProfile implements PathHolder {

    private String login;
    private final SocketChannel curChannel;
    private String authority;
    private final ContextData contextData;
    private final BiConsumer<String, SocketChannel> messageToUser;
    private long fileLength;

    UserProfile(String login, SocketChannel channel, BiConsumer<String, SocketChannel> messageToUser) {
        this.login = login;
        this.curChannel = channel;
        this.contextData = new ContextData();
        this.messageToUser = messageToUser;
    }

    @Override
    public void transComplete() {
        sendMessage("true%%%File uploaded successfully!");
        sendMessage("SYSTEM");

    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAuthority() {
        return authority;
    }

    public SocketChannel getCurChannel() {
        return curChannel;
    }

    public String getLogin() {
        return login;
    }

    public ContextData getContextData() {
        return contextData;
    }

    public void sendMessage(String message) {
        this.messageToUser.accept(message, this.curChannel);
    }

    @Override
    public void setFileLength(long length) {
        this.fileLength = length;
    }

    @Override
    public long getFileLength() {
        return this.fileLength;
    }
}
