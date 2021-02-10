package ru.johnnygomezzz;

import io.netty.channel.*;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.net.Socket;
import java.util.Scanner;

public class Client extends ChannelInboundHandlerAdapter {
    private static final int SIZE = 100*1024*1024;
    private static final int PORT = 8189;
    private static final String HOST = "localhost";

    public static void main(String[] args){

        try (Socket socket = new Socket(HOST, PORT);
             ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream in = new ObjectDecoderInputStream(socket.getInputStream(), SIZE))
        {
            System.out.println("Добро пожаловать в Хранилище №13!\n/help для подсказки.\n\n" +
                    "Введите команду:");

            while (true){
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();

                MyMessage textMessage = new MyMessage(msg);
                out.writeObject(textMessage);
                out.flush();

                MyMessage msgFromServer = (MyMessage) in.readObject();
                System.out.println("Ответ от сервера: " + msgFromServer.getText());
                if (msgFromServer.getText().equals("/quit")){
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


