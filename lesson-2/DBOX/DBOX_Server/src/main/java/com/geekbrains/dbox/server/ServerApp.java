package com.geekbrains.dbox.server;

import com.geekbrains.dbox.server.files.AllLinksFiles;
import com.geekbrains.dbox.server.user.Users;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerApp {
    public static void main(String[] args) {
        AllLinksFiles links = new AllLinksFiles();
        Users users = new Users();
        users.add("user", "user", "userrr");


        //Два пула потоков
            //Отвечает за подключение клиентов
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            //Отвечает за обработку потоков
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            //Выполняется преднастройка сервера
            ServerBootstrap b = new ServerBootstrap();
                //указываем серверу пулы потоков дляработы
            b.group(bossGroup, workerGroup)
                    //используем стандартный сервер НИО
                    .channel(NioServerSocketChannel.class)
                    //Обработчик отвечает за установку соедеинеия с клиентом
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //Добавляем обработчик в конец очереди
                            socketChannel.pipeline().addLast(new MainHandler());
                        }
                    });
            //Запуск сервера
            ChannelFuture future = b.bind(8888).sync();
            //Ожидание остановки сервера
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //Завершение работы пула потоков
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
