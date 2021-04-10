package ru.daniilazarnov;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;


public class ServerHandler {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ServerHandler(String host, int port) {
        try {
            socket = new Socket(host, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (ConnectException e) {
            System.out.println("!- Server is down.");
        } catch (Exception e) {
            throw new RuntimeException("SWW", e);
        }
    }

    public void sendMessage(MessageHandler handler) throws RuntimeException {
        try {
            out.write(handler.message, 0, handler.message.length);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        }
    }

    public String receiveMessage() throws IOException {
        return in.readUTF();
    }
}
