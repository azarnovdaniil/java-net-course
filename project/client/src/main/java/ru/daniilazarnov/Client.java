package ru.daniilazarnov;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    Socket socket;
    final String IP_ADDRESS = "localhost";
    final int PORT = 8189;

    DataInputStream in;
    DataOutputStream out;

    public static void main(String[] args) {

    }

    private void connect() {

        try {
            socket = new Socket(IP_ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    //Цикл работы
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/")) {
                           //список команд для клиентской стороны
                        }
                    }
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
