package ru.daniilazarnov;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

public class Users {
    private List<Channel> channels = new ArrayList<>();
    private String clientName;

    public List<Channel> getChannels() {
        return channels;
    }

    public String getClientName() {
        return clientName;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
