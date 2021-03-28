package ru.daniilazarnov;

import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Server {


    public static void main(String[] args) throws Exception {
        System.out.println("Server!");
        //для работы на стороне сервака создали 2 пула потоков
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);  // этот пул отвечает за подключающихся клиентов
        EventLoopGroup workGroup = new NioEventLoopGroup(); //пул отвечает за обработку данных
        try {


            ServerBootstrap b = new ServerBootstrap();//предназначен для преднастройки нашего сервера
            b.group(bossGroup, workGroup) //просим использовать сервергруп 2 потока 1 для подключение клиентов другой за работу
                    .channel(NioServerSocketChannel.class) //создаем канал для подключения клиентов
                    .childHandler(new ChannelInitializer<SocketChannel>() { //настраиваем процесс общения с клиентоми
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //в данном месте инициализируется клиент
                            socketChannel.pipeline().addLast(new ServerHandler());
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
