package exercise.serialization;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import exercise.serialization.domain.MyMessage;

import java.net.Socket;

public class Client {

    public static final int PORT = 8189;
    public static final int MAX_OBJECT_SIZE = 100 * 1024 * 1024;

    public static void main(String[] args) {

        try (Socket socket = new Socket("localhost", PORT);
             ObjectEncoderOutputStream oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream odis = new ObjectDecoderInputStream(socket.getInputStream(), MAX_OBJECT_SIZE)) {
            MyMessage textMessage = new MyMessage("Hello Server!!!");

            oeos.writeObject(textMessage);
            oeos.flush();
            MyMessage msgFromServer = (MyMessage) odis.readObject();
            System.out.println("Answer from server: " + msgFromServer.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
