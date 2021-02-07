package ru.daniilazarnov;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientServerConnection {

    private static Socket socket;
    private static ObjectEncoderOutputStream outStream;
    private static ObjectDecoderInputStream incStream;


    public static void startConnect(){
        try{
            Socket socket = new Socket("localhost", 8189);
            outStream = new ObjectEncoderOutputStream(socket.getOutputStream());
            incStream = new ObjectDecoderInputStream(socket.getInputStream(), 20971520);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopConnect(){
        try{
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            incStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
