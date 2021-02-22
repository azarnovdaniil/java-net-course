import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import server.ServerHandler;


public class Server {

    private static final int PORT = 8189;
    private static final int SIZE_1024 = 1024;
    private static final int SIZE_100 = 100;


    public static void main(String[] args) throws Exception {
        new Server().server();
    }

    public void server() throws InterruptedException {


        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline()
                            .addLast(
                                    new ObjectDecoder(SIZE_100 * SIZE_1024 * SIZE_1024,
                                            ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new ServerHandler()
                            );
                }
            });

            ChannelFuture future = b.bind(PORT).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
