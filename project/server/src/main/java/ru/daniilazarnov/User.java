package ru.daniilazarnov;

import io.netty.channel.Channel;

public class User {
   private Channel channel;
   private String ClientName;
   private String HomeDirectory;

    public User(Channel channel, String clientName, String homeDirectory) {
        this.channel = channel;
        ClientName = clientName;
        HomeDirectory = homeDirectory;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getHomeDirectory() {
        return HomeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
        HomeDirectory = homeDirectory;
    }
}
