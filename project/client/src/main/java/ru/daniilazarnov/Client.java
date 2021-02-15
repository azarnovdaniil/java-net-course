package ru.daniilazarnov;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.*;
import ru.daniilazarnov.handler.ClientOutHandler;

import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            new Bootstrap().group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast(new ClientOutHandler());
                        }
                    })
                    .connect("localhost", 8189)
                    .sync()
                    .channel()
                    .closeFuture()
                    .sync();
        } finally {
            group.shutdownGracefully();
        }

        try (Socket socket = new Socket("localhost", 8189);
             ObjectEncoderOutputStream encoder = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream decoder = new ObjectDecoderInputStream(socket.getInputStream(), 100 * 1024 * 1024)) {

            var scanner = new Scanner(System.in);

            Message message;
            while (true) {
                message = new Message(scanner.nextLine());
                encoder.writeObject(message);
                encoder.flush();
                if (message.getText().startsWith("/end")) break;
            }

            //сделать поток для приемки сообщения (либо настрооить Loop)
            Message msgFromServer = (Message) decoder.readObject();
            System.out.println("Answer from server: " + msgFromServer.getText());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
