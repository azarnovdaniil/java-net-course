package ru.daniilazarnov;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;
//import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Класс содержит реализацию сетевого соединения
 */
public class Network {
    private static final Logger log = Logger.getLogger(Network.class);
    private SocketChannel channel;

    private static final String HOST = "localhost";
    private static final int PORT = 8189;

    public Network() {
        Thread t = new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel){
                                channel = socketChannel;
                                socketChannel.pipeline().addLast(new ClientHandler());
//                                log.info("Соединение с сервером установлено!");
                            }
                        });
                ChannelFuture future = b.connect(HOST, PORT).sync();

                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        });
        t.start();
    }

    public String close() {
        channel.close();
        return "Соединение разорвано";
    }

    public boolean isConnect() {
        if (channel != null)
            return channel.isActive();
        else return false;
    }

    public void sendFile(String file) {
        try {
            FileSender.setLoadingStatus(true);
            FileSender.sendFile(Path.of(file), channel, getChannelFutureListenerSendFile("Файл успешно передан\n"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @NotNull
    private ChannelFutureListener getChannelFutureListenerSendFile(String s) {
        return future -> {
            if (!future.isSuccess()) {
                System.err.println(s + "не был");
            }
            if (future.isSuccess()) {
                System.out.print(s);
                FileSender.setLoadingStatus(false);

            }
        };
    }

//    @NotNull
    private ChannelFutureListener getChannelFutureListener(String s) {
        return future -> {
            if (!future.isSuccess()) {
                System.err.println(s + "не был");
            }
            if (future.isSuccess()) {
                System.out.print(s);
                FileSender.setLoadingStatus(false);

            }
        };
    }

    public void sendStringAndCommand(String fileName, byte command) {
        ReceivingAndSendingStrings.sendString(fileName, channel,command, getChannelFutureListener("\nИмя файла передано"));
    }
}
