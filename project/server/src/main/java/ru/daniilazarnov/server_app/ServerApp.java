package ru.daniilazarnov.server_app;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ru.daniilazarnov.auth.AuthService;
import ru.daniilazarnov.auth.BasicAuthService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class ServerApp {
    private static final int PORT = 8189;
    private Set<ChannelHandlerContext> clients;
    private AuthService authenticationService;
    private ClientHandler clientHandler;
    public ServerApp() {
        clients = new HashSet<>();
        authenticationService = new BasicAuthService();
        clientHandler = new ClientHandler();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerChannelInitializer(this, clientHandler));
            Path generalDirectory = Paths.get("/NetworkStorage");
            if(!Files.exists(generalDirectory)){
                Files.createDirectory(generalDirectory);
            }
            ChannelFuture future = b.bind(PORT).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public AuthService getAuthenticationService() {
        return authenticationService;
    }

}
