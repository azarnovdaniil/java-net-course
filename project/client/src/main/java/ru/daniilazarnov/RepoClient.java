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
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Paths;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class RepoClient {
    private final int port;
    private final String host;
    private SocketChannel curChannel;
    private final ContextData contextData;
    private ClientPathHolder pathHolder;
    private BiConsumer <ContextData,UserProfile> reader;
    private Consumer<UserProfile> closeChannel;

    RepoClient (String host, int port, String pathToRepo){
        this.port=port;
        this.host=host;
        this.contextData=new ContextData();
        this.pathHolder = new ClientPathHolder(pathToRepo);
        this.reader=(context, profile) -> {new ClientCommandReader(context,pathHolder).run();};
        this.closeChannel=profile -> {
            System.out.println("Connection lost...");
            close();
        };

    }

    public void start() {
        new Thread (()-> {
            EventLoopGroup workGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workGroup)
                        .channel(NioSocketChannel.class)
                        .remoteAddress(new InetSocketAddress(this.host, this.port))
                        .handler(new ChannelInitializer<SocketChannel>() {

                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                curChannel = socketChannel;
                                System.out.println("Connecting to server...");

                                socketChannel.pipeline().addLast(
                                        new DelimiterBasedFrameDecoder(8000, contextData.getDelimiter()));
                                socketChannel.pipeline().addLast(new RepoDecoder(false,reader, closeChannel));
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

        }).start();
    }

    public void execute (ContextData context){
        this.contextData.clone(context);
        if (context.getCommand()==CommandList.upload.ordinal()) {
            try {
                this.contextData.clone(context);
                File toCheck = Paths.get(context.getFilePath()).toFile();
                this.curChannel.writeAndFlush(new ChunkedFile(toCheck, 1024));
                this.contextData.setFilePath(toCheck.getName());
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("SWW with writing in channel");
            }
        }else this.curChannel.writeAndFlush(new byte[1]);
    }

    public void close(){
        System.out.println("Connection closed.");
        this.curChannel.close();

    }

    public void setRepoPath (String repoPath){
        this.pathHolder.setAuthority(repoPath);
    }


}

