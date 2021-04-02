package ru.daniilazarnov;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class RepoClient implements Runnable {
    private final int port;
    private final String host;
    private SocketChannel curChannel;
    private final ContextData contextData;
    private final ClientPathHolder pathHolder;
    private final Consumer<ContextData> reader;
    private final Consumer<Boolean> closeChannel;
    ClientCommandReader comReader;


    RepoClient(String host, int port, ContextData conData, ClientPathHolder pathHolder,
               ClientCommandReader comReader, Consumer<Runnable> addThread) {
        this.port = port;
        this.host = host;
        this.contextData = conData;
        this.pathHolder = pathHolder;
        this.comReader = comReader;
        this.reader = (context) -> {
            contextData.clone(context);
            addThread.accept(comReader);
        };
        this.closeChannel = a -> {
            System.out.println("Connection lost...");
            close();
        };

    }

    @Override
    public void run() {
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(this.host, this.port))
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            curChannel = socketChannel;
                            System.out.println("Connecting to server...");

                            socketChannel.pipeline().addLast(
                                    new DelimiterBasedFrameDecoder(8000, contextData.getDelimiter()));
                            socketChannel.pipeline().addLast(new RepoDecoderClient(reader, closeChannel));
                            socketChannel.pipeline().addLast(new RepoEncoder(contextData));
                            socketChannel.pipeline().addLast(new ChunkedWriteHandler());
                            socketChannel.pipeline().addLast(new IncomingFileHandler(pathHolder));


                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("SWW with connection.");
        } finally {
            try {
                workGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException("SWW with connection closing");
            }
        }

    }

    public synchronized void execute(ContextData context) {
        this.contextData.clone(context);
        if (context.getCommand() == CommandList.upload.getNum()) {
            try {
                this.contextData.clone(context);
                File toCheck = Paths.get(context.getFilePath()).toFile();
                ChunkedFile chunk = new ChunkedFile(toCheck, 1024);
                this.curChannel.writeAndFlush(chunk);
                this.contextData.setFilePath(toCheck.getName());
                wait();
                chunk.close();

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("SWW with writing in channel");
            }
        } else this.curChannel.writeAndFlush(new byte[1]);
    }

    public void close() {
        System.out.println("Connection closed.");
        this.curChannel.close();

    }

    public synchronized void goOn() {
        notify();
    }


}


