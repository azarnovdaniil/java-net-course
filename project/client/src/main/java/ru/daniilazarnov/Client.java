package ru.daniilazarnov;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 8189);
             ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream in = new ObjectDecoderInputStream(socket.getInputStream(), 100 * 1024 * 1024);) {

            while (true) {
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();

                File textMessage = new File(msg);
                out.writeObject(textMessage);
                out.flush();


                if (msg.startsWith("/")) {
                    if (msg.equals("/all")) {

                        try {
                            File fileFromServer = (File) in.readObject();
                            System.out.println(fileFromServer.getFile());
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    }


//                    MyMessage messageFromServer = (MyMessage) in.readObject();
//                    System.out.println(messageFromServer.getText());


                }

            }
        }
    }
}