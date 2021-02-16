package ru.daniilazarnov;

import io.netty.channel.Channel;

public class User {
   private Channel channel;
   private String clientName;
   private String homeDirectory;

    public User(Channel channel, String clientName, String homeDirectory) {
        this.channel = channel;
        this.clientName = clientName;
        this.homeDirectory = homeDirectory;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }
}
