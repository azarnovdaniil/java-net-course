package ru.daniilazarnov;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class RepoServer {
    private final int port;
    private final String host;
    private final LinkedList<UserProfile> userList;
    private BiConsumer<String, SocketChannel> sendServerMessage;
    private BiConsumer <ContextData,UserProfile> reader;
    private Consumer <UserProfile> closeConnection;
    private ExecutorService executorService;
    public static AuthorisationService authorisationService;




    RepoServer (String host, int port){
        this.port=port;
        this.host=host;
        this.userList = new LinkedList<UserProfile>();
        this.executorService = Executors.newCachedThreadPool();
        authorisationService = new AuthorisationService(this.userList);

        this.sendServerMessage= (mess, channel) -> {
            UserProfile toSend=null;
            for (UserProfile a:userList) {
                if(a.getCurChannel().equals(channel)){
                    toSend=a;
                    break;
                }
                throw new RuntimeException("No channel found for sending message!");
            }
            toSend.getContextData().setCommand(CommandList.serverMessage.ordinal());
            channel.writeAndFlush(mess.getBytes());
        };

        this.reader = (context, profile) -> this.executorService.submit(new ServerCommandReader(context,profile));

        this.closeConnection = (profile -> {
            this.userList.remove(profile);
            profile.getCurChannel().close();
            System.out.println("User left");
        });
    }

    public void start(){
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(workGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(this.host,this.port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    UserProfile connectedUser = new UserProfile("empty",socketChannel, sendServerMessage);
                    userList.add(connectedUser);
                    System.out.println("New connection applied.");

                    socketChannel.pipeline().addLast(
                            new DelimiterBasedFrameDecoder(8000, connectedUser.getContextData().getDelimiter()));
                    socketChannel.pipeline().addLast(new RepoDecoder(true, reader,closeConnection, connectedUser));
                    socketChannel.pipeline().addLast(new RepoEncoder(connectedUser.getContextData()));
                    socketChannel.pipeline().addLast(new ChunkedWriteHandler());
                    socketChannel.pipeline().addLast(new IncomingFileHandler(connectedUser));

                }
            });
            System.out.println("Server is up and ready for connections...");
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            try {
                workGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

