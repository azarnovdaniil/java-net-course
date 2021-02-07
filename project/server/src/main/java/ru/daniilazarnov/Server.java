package ru.daniilazarnov;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Server {
    public static void main(String[] args) {

        EventLoopGroup parentGroup = new NioEventLoopGroup(1);  // пул потоков для инициализации подключений
        EventLoopGroup childGroup = new NioEventLoopGroup(); // пул потоков для обработки данных

        try {
            ServerBootstrap srvBoot = new ServerBootstrap(); // установка параметров сервера
            srvBoot.group(parentGroup, childGroup); // установка пулов потоков
            srvBoot.channel(NioServerSocketChannel.class);  // не понимаю эту запись, канал для подключения клиентов,

            ChannelInitializer channelInit = new ChannelInitializer<SocketChannel>() { // не понимаю эту запись, SocketChannel информация о соединении
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {

                }
            };

            srvBoot.childHandler(channelInit); // не понимаю эту запись, настройка процесса общения,

            ChannelFuture future = srvBoot.bind(50505).sync(); // привязка порта, запуск сервера, future - информация о какой-то выполняемой задаче.
            future.channel().closeFuture().sync(); // loseFuture() - ожидание остановки сервера, sync() - запуск ожидания,

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            parentGroup.shutdownGracefully(); // закрытие пула потоков
            childGroup.shutdownGracefully(); // закрытие пула потоков
        }
    }
}
