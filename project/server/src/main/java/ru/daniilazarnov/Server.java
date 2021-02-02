package ru.daniilazarnov;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;


public class Server {
    private static final Logger log = Logger.getLogger(Server.class);

    public void run() throws Exception {
        // Пул потоков для обработки подключений клиентов
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // Пул потоков для обработки сетевых сообщений
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // Создание настроек сервера
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup) // указание пулов потоков для работы сервера
                    .channel(NioServerSocketChannel.class) // указание канала для подключения новых клиентов
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception { // настройка конвеера для каждого подключившегося клиента
                            ch.pipeline().addLast(new StringToByteBufHandler(), new FirstHandler(), new SecondHandler(), new GatewayHandler(), new FinalHandler());
                        }
                    });
            ChannelFuture f = b.bind(8189).sync(); // запуск прослушивания порта 8189 для подключения клиентов
            log.info("SERVER: Запустился, жду подключений...");
            f.channel().closeFuture().sync(); // ожидание завершения работы сервера
        } finally {
            workerGroup.shutdownGracefully(); // закрытие пула
            bossGroup.shutdownGracefully(); // закрытие пула
        }
    }

    public static void main(String[] args) throws Exception {
        new Server().run();

    }

}
