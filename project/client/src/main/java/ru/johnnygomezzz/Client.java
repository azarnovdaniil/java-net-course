package ru.johnnygomezzz;

import io.netty.channel.*;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
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

                //String str = String.valueOf(in.readObject());
                System.out.println(msg);


            }
        }
    }


}
