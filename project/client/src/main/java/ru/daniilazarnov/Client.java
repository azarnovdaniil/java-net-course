package ru.daniilazarnov;

import ru.daniilazarnov.data.*;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;

public class Client {


    private Socket socket;
    private final String IP_ADDRESS = "localhost";
    private final int PORT = 8894;


    private Reader inputStreamReader;
    private ObjectOutputStream objectOutputStream;

    public static void main(String[] args) {

        Client client = new Client();
        client.connect();

        System.out.println("Client!");




    }


    public void connect () {
        try {
            this.socket = new Socket(IP_ADDRESS, PORT);
            this.inputStreamReader = new InputStreamReader(this.socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        int data = this.inputStreamReader.read();
                        while(data != -1) {
                            data = inputStreamReader.read();
                        }

                    }
                } catch (IOException e) {
                        e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    if (!this.socket.isConnected()) {
                        return;
                    }
                    Thread.sleep(3000);

                    FileData fileData = new FileData(TypeMessages.FILE);
                    fileData.addFile(Paths.get("files/demo.jpg"));
                    objectOutputStream.writeObject(fileData);
                    objectOutputStream.flush();

                    Thread.sleep(3000);

                    objectOutputStream.writeObject(new CommonData(TypeMessages.AUTH));
                    objectOutputStream.flush();



                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
