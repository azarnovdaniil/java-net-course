package server;

import java.nio.channels.Channel;

public class User {
    private Channel channel;
    private String userName;
    private String directory;

    public User(Channel channel, String userName, String directory) {
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

