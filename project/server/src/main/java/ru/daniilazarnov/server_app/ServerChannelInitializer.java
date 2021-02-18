package ru.daniilazarnov.server_app;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private ServerApp serverApp;
    public ClientHandler clientHandler;
    public ServerChannelInitializer(ServerApp serverApp, ClientHandler clientHandler){
        this.serverApp = serverApp;
        this.clientHandler=clientHandler;
    }
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(
                new MainHandler(serverApp, clientHandler));
    }
}
