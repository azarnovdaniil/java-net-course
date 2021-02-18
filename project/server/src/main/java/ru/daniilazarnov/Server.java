package ru.daniilazarnov;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;


/**
 * Сервер для обмена файлами. Используется фреймворк Netty.
 * Сервер на вход принимает только сериализованные объекты MessagePacket, размер в байтах которых ограничен 1048576 байт.
 * Сервер
 *
 * */
public class Server {

    public static void main(String[] args) {

        EventLoopGroup clientGroup = new NioEventLoopGroup(1); //Создаем первый менеджер потоков для инициализации клиентов

        EventLoopGroup dataManagementGroup = new NioEventLoopGroup(); //Создаем второй менеджер потоков для управления передачей данных
        //теперь настроим сервер
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(clientGroup, dataManagementGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ObjectDecoder(1024 * 1024 * 100, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new DServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = b.bind(8189).sync();
            System.out.println("Server started!");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clientGroup.shutdownGracefully();
            dataManagementGroup.shutdownGracefully();
        }
    }
}
