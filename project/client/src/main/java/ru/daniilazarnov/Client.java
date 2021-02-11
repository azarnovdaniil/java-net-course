package ru.daniilazarnov;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

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
