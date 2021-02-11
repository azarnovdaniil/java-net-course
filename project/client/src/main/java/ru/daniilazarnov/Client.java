package ru.daniilazarnov;
import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
/**
 *   Комманды для сервера:
 *   UPLOAD     - Отправка файлов на сервер;
 *   DOWNLOAD   - Скачивание файлов с сервера; *
 *   SHOW       - Просмотр файлов на сервере для каждого пользователя;
 *   REMOVE     - Удаление файлов локально/на сервере;
 *   RENAME     - Переименование файлов локально/на сервере;
 */


public class Client {

    static final String HOST = "127.0.0.1";
    static final int PORT = 8189;
    static String clientName;
    static String clientKEY;

    public static void main(String[] args) throws Exception {

        Commands commands = new Commands();
        commands.commands.add("UPLOAD");
        commands.commands.add("DOWNLOAD");
        commands.commands.add("SHOW");
        commands.commands.add("REMOVE");
        commands.commands.add("RENAME");

        /*
         * Get name of the user for this session.
         */
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your name: ");
        if (scanner.hasNext()) {
            clientName = scanner.nextLine();
            System.out.println("Welcome " + clientName);
        }
        System.out.println("Please create (enter) your SECRET KEYWORD to identify your use of our cloud file service: ");
        if (scanner.hasNext()) {
            clientKEY = scanner.nextLine();
            System.out.println("Your SECRET KEYWORD is:" + clientKEY);
        }
        /*
         * Configure the client.
         */

        // Since this is client, it doesn't need boss group. Create single group.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group) // Set EventLoopGroup to handle all events for client.
                    .channel(NioSocketChannel.class)// Use NIO to accept new connections.
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            /*
                             * Socket/channel communication happens in byte streams. String decoder &
                             * encoder helps conversion between bytes & String.
                             */
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());

                            // This is our custom client handler which will have logic for chat.
                            p.addLast(new ClientHandler());

                        }
                    });

            // Start the client.
            ChannelFuture f = b.connect(HOST, PORT).sync();

            /*
             * Iterate & take chat message inputs from user & then send to server.
             */
            while (scanner.hasNext()) {
                String input = scanner.nextLine();
                Channel channel = f.sync().channel();
                channel.writeAndFlush("[" + clientName + "]: " + input);
                channel.flush();
            }

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }
}