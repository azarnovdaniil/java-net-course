package ru.daniilazarnov.auth;

import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.net.Socket;

public class AuthClient {

    public static final int PORT = 8189;
    public static final String HOST = "localhost";

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream())) {

            out.writeObject("/auth Vasya");
            out.writeObject("Hello");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
