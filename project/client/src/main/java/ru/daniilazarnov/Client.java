package ru.daniilazarnov;

import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static final int PORT = 8189;
    public static final String LOCALHOST = "localhost";

    private Socket socket;
    private DataOutputStream out;
    private Scanner in;

    //todo args
    public static void main(String[] args) {
        try {
            new Client().run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Client() throws IOException {
        this.socket = new Socket(InetAddress.getLocalHost(), PORT);
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in  = new Scanner(socket.getInputStream());
    }

    private void run() {
        auth();
    }

    public boolean auth(){
        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream())) {

            out.writeObject("/auth Vasya");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
