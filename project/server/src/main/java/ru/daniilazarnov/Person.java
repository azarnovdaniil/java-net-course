package ru.daniilazarnov;

import io.netty.channel.Channel;

public class Person {
    private Channel channel;
    private String userName;
    private String directory;

    public Person(Channel channel, String userName, String directory) {
        this.channel = channel;
        this.userName = userName;
        this.directory = directory;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getUserName() {
        return userName;
    }

    public String getDirectory() {
        return directory;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
