package ru.daniilazarnov;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

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



    public void close() {
        channel.close();
    }


    public void sendMessage(String str) {
        channel.writeAndFlush(str);
        log.info("Сообщение отправлено");
    }

    public boolean isConnect() {
        if (channel != null)
            return channel.isActive();
        else return false;
    }

    public void sendFile(String file) {
        try {
            FileSender.sendFile(Path.of(file), channel, getChannelFutureListener("\nФайл успешно передан"));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        log.info("Сообщение отправлено");
    }

    @NotNull
    private ChannelFutureListener getChannelFutureListener(String s) {
        return future -> {
            if (!future.isSuccess()) {
                System.err.println(s + "не был");
//                log.error(future.cause());

//                    future.cause().printStackTrace();
            }
            if (future.isSuccess()) {
                System.out.print(s);
//                    channel.disconnect();
//                    channel.close();
//                    Network.getInstance().stop();
            }
        };
    }

    public void sendByte(int  i) {
        byte b = (byte) i;
        try {
            SendCommandByte.sendByte(b, channel, getChannelFutureListener("\nУправляющий байт передан"));
        } catch (IOException e) {
            e.printStackTrace();
        }




//        log.info("Сообщение отправлено");
    }

    public void sendFileName(String fileName) {
        SendFileName.sendFileName(fileName, channel, getChannelFutureListener("\nИмя файла передано"));
    }

}
