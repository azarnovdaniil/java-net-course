package ru.daniilazarnov;

import java.nio.channels.Channel;

public class User {
    private String name;
    private String address;
    private Channel channel;

    public User(String name, String address, Channel channel){
        this.name = name;
        this.address = address;
        this.channel = channel;
    }

    public String getName () {
        return name;
    }

    public String getAddress () {
        return address;
    }

    public Channel getChannel () {
        return channel;
    }

    public void setChannel (Channel channel) {
        this.channel = channel;
    }
}
