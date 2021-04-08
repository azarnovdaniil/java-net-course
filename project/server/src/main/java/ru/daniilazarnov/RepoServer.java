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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class RepoServer {

    private static ExecutorService executorService;
    private static LinkedList<UserProfile> userList;
    private final BiConsumer<String, SocketChannel> sendServerMessage;
    private final BiConsumer<ContextData, UserProfile> reader;
    private final Consumer<UserProfile> closeConnection;
    private static final Logger LOGGER = LogManager.getLogger(RepoServer.class);

    /**
     * Channel manager class. Keeps connected UserProfile list and manages it.
     * Keeps and  manages server executor service. Manages channel activity.
     */
    RepoServer() {
        ServerConfigReader.readConfig();
        userList = new LinkedList<UserProfile>();
        executorService = Executors.newCachedThreadPool();
        AuthorisationService.initAuthorisationService(userList);

        this.sendServerMessage = (mess, channel) -> {
            UserProfile toSend = null;
            for (UserProfile a : userList) {
                if (a.getCurChannel().equals(channel)) {
                    toSend = a;
                    break;
                }
                LOGGER.error("No channel found for sending message!");
            }
            assert toSend != null;
            toSend.getContextData().setCommand(CommandList.serverMessage.getNum());
            channel.writeAndFlush(mess.getBytes());
        };

        this.reader = (context, profile) -> executorService.submit(new Thread(() -> profile.executeMessage(context)));

        this.closeConnection = (profile -> {
            userList.remove(profile);
            profile.getCurChannel().close();
            LOGGER.info("User left");
        });
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(ServerConfigReader.getHost(), ServerConfigReader.getPort()))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            UserProfile connectedUser = new UserProfile("empty", socketChannel, sendServerMessage);
                            userList.add(connectedUser);
                            LOGGER.info("New connection applied.");

                            socketChannel.pipeline().addLast(
                                    new DelimiterBasedFrameDecoder(2048, connectedUser.getContextData().getDelimiter()));
                            socketChannel.pipeline().addLast(new RepoDecoder<UserProfile>(reader, closeConnection, connectedUser));
                            socketChannel.pipeline().addLast(new RepoEncoder(connectedUser.getContextData()));
                            socketChannel.pipeline().addLast(new ChunkedWriteHandler());
                            socketChannel.pipeline().addLast(new IncomingFileHandler(connectedUser));

                        }
                    });
            LOGGER.info("Server is up and ready for connections...");
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            LOGGER.error("SWW with connection.", LOGGER.throwing(e));
        } finally {
            try {
                bossGroup.shutdownGracefully().sync();
                workGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                LOGGER.error("SWW closing loo[groups.", LOGGER.throwing(e));
            }
        }
    }

}

