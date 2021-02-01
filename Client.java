package ru.daniilazarnov;

import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws Exception {
        new Client();
    }

    public Client(){
        try {
            Socket clientSocket = new Socket("127.0.0.1", 888);

            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            Thread th1 = new Thread(){
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        String servMessage = null;
                        try {
                            servMessage = in.readUTF();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println(servMessage);
                    }
                }
            };

            Thread th2 = new Thread(){
                @Override
                public void run() {
                    super.run();
                    while (true){
                        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                        String message = null;
                        try {
                            message = reader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.writeUTF(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            th1.start();
            th2.start();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
