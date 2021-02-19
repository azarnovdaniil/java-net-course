package ru.daniilazarnov;

import io.netty.channel.Channel;

public class User {
   private String clientName;
   private String homeDirectory;

    public User(String clientName, String homeDirectory) {
        this.clientName = clientName;
        this.homeDirectory = homeDirectory;
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
