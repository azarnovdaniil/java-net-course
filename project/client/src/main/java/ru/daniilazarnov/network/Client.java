package ru.daniilazarnov.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;
import ru.daniilazarnov.FileSender;
import ru.daniilazarnov.ReceivingAndSendingStrings;
import ru.daniilazarnov.console_IO.OutputConsole;
//import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Класс содержит реализацию сетевого соединения
 */
public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class);
    private SocketChannel channel;

    private static final String HOST = "localhost";
    private static final int PORT = 8189;

    public Client() {
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
                                socketChannel.pipeline().addLast(
                                        new ClientReadMessage());
                            }
                        });
                LOG.debug("Connection established");
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
        if (channel != null) {
            return channel.isActive();
        } else {
            return false;
        }
    }

    public void sendFile(String file) {
        try {
            FileSender.setLoadingStatus(true); // удалить
            FileSender.sendFile(Path.of(file), channel, getChannelFutureListenerSendFile("Файл успешно передан\n"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ChannelFutureListener getChannelFutureListenerSendFile(String s) {
        return (ChannelFuture future) -> {
            if (!future.isSuccess()) {
                LOG.info(s + "не был");
            }
            if (future.isSuccess()) {
                LOG.debug(s);
                FileSender.setLoadingStatus(false); // удалить
            }
            OutputConsole.setConsoleBusy(false);
        };
    }

    private ChannelFutureListener getChannelFutureListener(String s) {
        return future -> {
            if (!future.isSuccess()) {
                LOG.info(s + "не был");
            }
            if (future.isSuccess()) {
                FileSender.setLoadingStatus(false); // удалить
                LOG.info(s);
            }
            OutputConsole.setConsoleBusy(false);
        };
    }

    public void sendStringAndCommand(String fileName, byte command) {
        ReceivingAndSendingStrings.sendString(fileName, channel, command,
                getChannelFutureListener("\nИмя файла передано"));
    }
}
