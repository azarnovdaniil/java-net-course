package ru.daniilazarnov;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


/**
 * Сервер для обмена файлами. Используется фреймворк Netty.
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
                    .channel(NioServerSocketChannel.class)  //создаем канал для подключения
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new StringEncoder());
                            socketChannel.pipeline().addLast(new ServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = b.bind(8189).sync(); //  Указываем, что сервер должен стартовать на порту 8189. "sinc()" - "выполнить задачу"

            System.out.println("Server started!");
            future.channel().closeFuture().sync();  // блокирующая операция  - ожидания закрытия канала. Как только серевер будет остановлен, то попадаем в finaly.
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            clientGroup.shutdownGracefully(); // закрываем первый пулл потоков
            dataManagementGroup.shutdownGracefully(); // закрываем второй пулл потоков
        }
    }
}
