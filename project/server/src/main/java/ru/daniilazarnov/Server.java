package ru.daniilazarnov;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final  int SEVER_PORT = 8888;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;

    public Server () {
        try {
            System.out.println("Server: starting up....");
            serverSocket = new ServerSocket(SEVER_PORT);

            System.out.println("Server: waiting for a connection...");
            clientSocket = serverSocket.accept();
            System.out.println("Server: client " + clientSocket +  " is connected");

            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            while (true) {
                String incomingMessage = in.readUTF();
                if (incomingMessage.contains("-exit")) {
                    out.writeUTF("cmd Exit");
                    break;
                }
                out.writeUTF("Echo: " + incomingMessage);
            }

            System.out.println("Server: socket shutdown");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new Server();
        System.out.println("Server!");
    }
}
