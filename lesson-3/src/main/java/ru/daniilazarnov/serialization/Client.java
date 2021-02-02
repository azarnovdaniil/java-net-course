package ru.daniilazarnov.serialization;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import ru.daniilazarnov.serialization.domain.MyMessage;

import java.net.Socket;

public class Client {

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", 8189);
             ObjectEncoderOutputStream oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream odis = new ObjectDecoderInputStream(socket.getInputStream(), 100 * 1024 * 1024)) {
            MyMessage textMessage = new MyMessage("Hello Server!!!");

            ///FileMessage fileMessage = new FileMessage("file.txt", Files.readAllBytes(Path.of("/Users/daniilazarnov/repos/java-course-3/demo.txt")));
            oeos.writeObject(textMessage);
            oeos.flush();
            MyMessage msgFromServer = (MyMessage) odis.readObject();
            System.out.println("Answer from server: " + msgFromServer.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
