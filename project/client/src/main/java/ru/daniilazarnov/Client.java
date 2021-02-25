package ru.daniilazarnov;


import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;



public class Client extends ChannelInboundHandlerAdapter {


    private static ObjectEncoderOutputStream out;
    private static ObjectDecoderInputStream in;
    private static final int PORT = 8189;
    private static final int SIZE_1024 = 1024;
    private static final int SIZE_100 = 100;


    public static void main(String[] args) {

//            ClientHandler network = new ClientHandler();
            ClientHandler.start();

    }

}
