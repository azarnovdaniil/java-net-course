package ru.daniilazarnov;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Client {

    public static final int PORT = 8888;
    private Channel currentChannel;
    public static final byte MAGIC_BYTE = (byte) 25;
    public static File file = new File("1.txt");

    public Client() {
    }

    public void run(Path path) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress("localhost", PORT))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast();
                            currentChannel = socketChannel;
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect().sync();
            sendFile(path, future -> {
                if (!future.isSuccess()) {
                    future.cause().printStackTrace();
                    stop();
                }
                if (future.isSuccess()) {
                    System.out.println("Файл успешно передан");
                    stop();
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private void sendFile(Path path, ChannelFutureListener finishListener) throws IOException {

        FileRegion region = new DefaultFileRegion(path.toFile(), 0, Files.size(path));

        ByteBuf buf;
        buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(MAGIC_BYTE);
        currentChannel.writeAndFlush(buf);

        byte[] filenameBytes = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
        buf = ByteBufAllocator.DEFAULT.directBuffer(Integer.SIZE / Byte.SIZE);
        buf.writeInt(filenameBytes.length);
        currentChannel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(filenameBytes.length);
        buf.writeBytes(filenameBytes);
        currentChannel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(Long.SIZE / Byte.SIZE);
        buf.writeLong(Files.size(path));
        currentChannel.writeAndFlush(buf);

        ChannelFuture transferOperationFuture = currentChannel.writeAndFlush(region);
        if (finishListener != null) {
            transferOperationFuture.addListener(finishListener);
        }
    }

    public void stop() {
        currentChannel.close();
    }

    public static void main(String[] args) {
        new Client().run(Paths.get(file.getName()));
    }
}
