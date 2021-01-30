package ru.daniilazarnov;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;


public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;


    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {


                try {
                    socket.setSoTimeout(120000); // через время нас отклчит
                } catch (SocketException e) {
                    e.printStackTrace();
                }

                while (true) {
                    String str = null;

                    try {
                        str = in.readUTF();    // считываем то что написали (не уверен что верно)

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(str);
                }

            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
