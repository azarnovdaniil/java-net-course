package ru.daniilazarnov;

import io.netty.channel.socket.SocketChannel;

public class UserProfile {

    private String login;
    private final SocketChannel curChannel;
    private String authority;

    UserProfile( String login, SocketChannel channel){
        this.login = login;
        this.curChannel=channel;
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
}
