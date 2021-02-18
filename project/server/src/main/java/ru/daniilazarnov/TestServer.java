package ru.daniilazarnov;

import java.net.*;
import java.io.*;

public class TestServer extends Thread {
    private ServerSocket serverSocket;

    public TestServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000000);
    }

    public static void main(String [] args) {
        int port = Integer.parseInt(args[0]);
        try {
            // ? do this in a seperate process?
            Thread t = new TestServer(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        while(true) {

            try {

                System.out.println("Waiting for client on port " +
                        serverSocket.getLocalPort());

                Socket server = serverSocket.accept();

                System.out.println("Just connected to " + server.getRemoteSocketAddress());

                DataInputStream input = new DataInputStream(server.getInputStream());
                DataOutputStream output = new DataOutputStream(server.getOutputStream());

                // initial interaction ** send menu
                output.writeUTF("\n\nSERVER>\n" + menu());

                int service = input.readInt();
                int input1 = input.readInt();
                int input2 = input.readInt();

            /*
            System.out.println("service: " + service);
            System.out.println("input1: " + input1);
            System.out.println("input2: " + input2);
            */

                do {

                    // ** evaluate request

                    if (service == 0) {
                        break; // from do while
                    } else if (service == 1) {

                        output.writeUTF("\n\nSERVER>\n" + menu());

                    } else if (service == 2) {

                        output.writeUTF("SERVER>\n\t " + add(input1, input2));

                    } else if (service == 3) {

                        output.writeUTF("SERVER>\n\t " + diff(input1, input2));

                    } else if (service == 4) {

                        output.writeUTF("SERVER>\n\t " + mult(input1, input2));

                    } else if (service == 5) {

                        output.writeUTF("SERVER>\n\t " + qout(input1, input2));

                    } else {
                        output.writeUTF("SERVER>\n\t " + "invalid choice");

                    }


                    service = input.readInt();
                    input1 = input.readInt();
                    input2 = input.readInt();

                } while (service != 0);

                output.writeUTF("SERVER>\n\t " + "Thank you for connecting to " + server.getLocalSocketAddress());

                server.close();



            } catch (SocketTimeoutException s) {

                System.out.println("Socket timed out");
                break;

            } catch (IOException e) {

                e.printStackTrace();
                break;

            }
        }
    }


    private String menu() {
        return "\tMath Server\n***************************\nchoose a number for the coresponding service\nthen send response in this format\n\n\tservice: (int)\n\tinput1: (int)\n\tinput2: (int)\n\n 0. Quit\n 1. Print this help message\n 2. Addition\n 3. Subtraction\n 4. Multiplication\n 5. Division";
    }

    private int add(int a, int b) {
        return a + b;
    }

    private int diff(int a, int b) {
        return a - b;
    }

    private int mult(int a, int b) {
        return a*b;
    }

    private double qout(int a, int b) {
        return (double)a/b;
    }
}