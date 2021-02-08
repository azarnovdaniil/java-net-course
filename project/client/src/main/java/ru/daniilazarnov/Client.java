package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import io.netty.util.ReferenceCountUtil;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends ChannelInboundHandlerAdapter {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try (Socket socket = new Socket("localhost", 8189);
             ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream in = new ObjectDecoderInputStream(socket.getInputStream(), 100 * 1024 * 1024)
        ) {

            while (true) {
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();

                String textMessage = msg;
                out.writeObject(textMessage);
                out.flush();

                boolean str = (Boolean) in.readObject();
                System.out.println(str);


            }
        }
    }


}