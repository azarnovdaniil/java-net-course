package ru.daniilazarnov;


import io.netty.channel.ChannelInboundHandlerAdapter;


public class Client extends ChannelInboundHandlerAdapter {

    public static void main(String[] args) {
        ClientHandler.start();
    }
}
