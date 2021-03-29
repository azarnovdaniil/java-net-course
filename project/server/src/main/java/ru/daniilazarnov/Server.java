package ru.daniilazarnov;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {


    public static void main(String[] args) throws Exception {
        System.out.println("Server!");
        //для работы на стороне сервака создали 2 пула потоков
        EventLoopGroup bossGroup = new NioEventLoopGroup();  // этот пул отвечает за подключающихся клиентов
        EventLoopGroup workGroup = new NioEventLoopGroup(); //пул отвечает за обработку данных
        try {
            ServerBootstrap b = new ServerBootstrap();//предназначен для преднастройки нашего сервера
            b.group(bossGroup, workGroup) //просим использовать сервергруп 2 потока 1 для подключение клиентов другой за работу
                    .channel(NioServerSocketChannel.class) //создаем канал для подключения клиентов
                    .childHandler(new ChannelInitializer<SocketChannel>() { //настраиваем процесс общения с клиентоми
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //в данном месте инициализируется клиент
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    });
            ChannelFuture future = b.bind(8188).sync();// стартуем сервер с портом 8189
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
