package exercise;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;
import exercise.handlers.HelloServerHandler;
import exercise.handlers.in.FinalInboundHandler;
import exercise.handlers.in.FirstInboundHandler;
import exercise.handlers.in.GatewayInboundHandler;
import exercise.handlers.in.SecondInboundHandler;
import exercise.handlers.out.StringToByteBufOutboundHandler;
import exercise.handlers.out.StringToStringOutboundHandler;

import java.util.Set;

public final class DiscardServer {

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // bossGroup отвечает за принятия сообщения
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // workerGroup отвечает за обработку pipeline
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // добавляем группы (группа распределения pipeline и группа самих pipeline)
            serverBootstrap.group(bossGroup, workerGroup)
            /* конфигурирует с помощью какого канала обрабатываются msg, в 99% случаев выбираем NioServerSocketChannel
             * это тот способ в котором будет указанна обработка сетевых взаимодействий */
                    .channel(NioServerSocketChannel.class)
                    .childHandler(/*эта конфигурация будет создаваться на каждое соединение*/new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new HelloServerHandler());
                }
            });
            /* говорим на каком порту работаем, можно указать хост(по умолчанию localhost)
             * метод sync() блокирующий метод, netty слушает... */
            ChannelFuture channelFuture = serverBootstrap.bind(8848).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully(); // когда канал будет закрыт, останавливаем worker и boss group
            bossGroup.shutdownGracefully();
        }
    }

    public void runManyPipeline() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline()
                                    // выполняться они будут в том же порядке что и записаны
                                    .addLast(new StringToStringOutboundHandler()) // 1 - outbound // можно передать','
                                    .addLast(new FirstInboundHandler()) // 1 - inbound
                                    .addLast(new StringToByteBufOutboundHandler()) // 2 - outbound
                                    .addLast(new SecondInboundHandler()) // 2 - inbound
                                    .addLast(new GatewayInboundHandler()) // 3 - inbound
                                    .addLast(new FinalInboundHandler()); // 4 - inbound
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(8848).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Пример авторизации
 */

class AuthServer implements Runnable {
    public static final int MAX_OBJECT_SIZE = 1024 * 1024;

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new ObjectDecoder(MAX_OBJECT_SIZE, null), new AuthHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(8848).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new Thread(new AuthServer()).start();
    }
}

class AuthHandler extends ChannelInboundHandlerAdapter {

    // здесь должен быть сервис, который проверяет наших пользователей
    private final Set<String> authorizedClients = Set.of("Bob");

    // т.к. pipeline создаётся для каждого клиента, мы храним внутри переменную, пройдена ли аутентификация
    private boolean authOk = false;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String input = (String) msg;

        if (authOk) {
            ctx.fireChannelRead(input);
            return;
        }
        if (input.split(" ")[0].equals("/auth")) {
            String username = input.split(" ")[1];
            if (authorizedClients.contains(username)) {
                authOk = true; // если авторизация пройдена
                ctx.pipeline().addLast(new MainHandler(username)); // создаём следующий pipeline
            }
        }
    }
}

class MainHandler extends ChannelInboundHandlerAdapter { // здесь делаем новых handler-ов

    private final String username;

    MainHandler(String username) {
        this.username = username;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String input = (String) msg;
        System.out.println(username + ": " + input);
    }
}