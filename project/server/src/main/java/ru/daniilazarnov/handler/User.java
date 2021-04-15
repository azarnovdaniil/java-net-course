package ru.daniilazarnov.handler;

import io.netty.channel.Channel;

public class User {
    private String name;
    private Channel channel;
    private TypeUser typeUser = TypeUser.CLI;

    public User(String name, Channel channel) {
        this.name = name;
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public Channel getChannel() {
        return channel;
    }

    public TypeUser getTypeUser() {
        return typeUser;
    }
}
