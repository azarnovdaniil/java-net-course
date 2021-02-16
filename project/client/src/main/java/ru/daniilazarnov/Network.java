package ru.daniilazarnov;

import java.io.*;
import java.net.Socket;

public final class Network {

    public static final int INET_PORT = 8189;
    public static final String HOSTNAME = "localhost";
    private static Network network;
    private DataInputStream is;
    private DataOutputStream os;

    private Network() {
        try {
            Socket socket = new Socket(HOSTNAME, INET_PORT);
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataInputStream getIn() {
        return is;
    }

    public DataOutputStream getOut() {
        return os;
    }

    public static Network get() {
        if (network == null) {
            network = new Network();
        }
        return network;
    }
}
