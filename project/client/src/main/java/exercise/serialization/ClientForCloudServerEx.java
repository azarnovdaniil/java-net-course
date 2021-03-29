package exercise.serialization;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import exercise.domain.MyMessageEx;

import java.net.Socket;

public class ClientForCloudServerEx {

    public static final int MAX_OBJECT_SIZE = 100 * 1024 * 1024;

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", 8848);
             ObjectEncoderOutputStream oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream odis = new ObjectDecoderInputStream(socket.getInputStream(), MAX_OBJECT_SIZE)) {
            MyMessageEx textMessage = new MyMessageEx("Hello Server!!!");

            oeos.writeObject(textMessage);
            oeos.flush();
            MyMessageEx msgFromServer = (MyMessageEx) odis.readObject();
            System.out.println("Answer from server: " + msgFromServer.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}