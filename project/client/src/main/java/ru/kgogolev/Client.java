package ru.kgogolev;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Client {
    private SocketChannel channel;
    String host = "localhost";
    int port = 9999;
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public Client() {
        new Thread(()->
        {
            try {
                Bootstrap b = new Bootstrap(); // (1)
                b.group(workerGroup); // (2)
                b.channel(NioSocketChannel.class); // (3)
//            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
                b.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
//                        ch.pipeline().addLast(new ClientHandler());
                        channel = ch;
                        ch.pipeline().addLast(new ClientHandler(), new StringEncoder());
                    }
                });

                // Start the client.
                ChannelFuture f = b.connect(host, port).sync(); // (5)

                // Wait until the connection is closed.
                f.channel().closeFuture().sync();
            }
            catch(Exception e) {
                e.printStackTrace();
            }finally
            {
                workerGroup.shutdownGracefully();
            }
        }).start();

    listenConsole();
    }
    public void sendMessage(String str){
        channel.writeAndFlush(str);
    }
    public void listenConsole(){
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            while (true){
                String line = br.readLine()+"\r\n";
                sendMessage(line);
//                channel.read();
//                System.out.println();
            }


        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

