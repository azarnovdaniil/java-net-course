package ru.johnnygomezzz;


import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.johnnygomezzz.handlers.ClientHandler;


public class Client extends ChannelInboundHandlerAdapter {

    public static void main(String[] args) {
        ClientHandler.start();
    }
}
