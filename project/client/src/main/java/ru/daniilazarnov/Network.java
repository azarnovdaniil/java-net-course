package client;

import common.FileSender;
import common.ReceivingString;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.nio.file.Path;


public class Network {
    private static String HOST = "localhost";
    private static  int PORT = 8181;
    private SocketChannel channel;
    private boolean statusNetwork = false;

    public Network() {
        Thread t = new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) {
                                channel = socketChannel;
                                socketChannel.pipeline().addLast(new ClientHandler());
                            }
                        });
                statusNetwork = true;
                ChannelFuture future = b.connect(HOST, PORT).sync();
                future.channel().closeFuture().sync();
                statusNetwork = false;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        });
        t.start();
    }
    public void sendFile(String file) {
        try {
            FileSender.setLoadingStatus(true);
            FileSender.sendFile(Path.of(file), channel, getChannelFutureListenerSendFile("Файл успешно передан\n"));
            System.out.println("2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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
    public boolean isConnect() {
        if (channel != null) {
            return channel.isActive();
        } else {
            return false;
        }
    }
    public void sendStringAndCommand(String fileName, byte command) {
        ReceivingString.sendString(fileName, channel, command,
                getChannelFutureListener("\nИмя файла передано"));
    }
}
