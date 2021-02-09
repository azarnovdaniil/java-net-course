package ru.johnnygomezzz;

import io.netty.channel.*;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.net.Socket;
import java.util.Scanner;

public class Client extends ChannelInboundHandlerAdapter {

    public static void main(String[] args){

        try (Socket socket = new Socket("localhost", 8189);
             ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream in = new ObjectDecoderInputStream(socket.getInputStream(),
                     100 * 1024 * 1024)) {


            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();

            while (true){
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


