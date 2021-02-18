package ru.daniilazarnov;

import java.net.*;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TestClient {

    public static void main(String [] args) {
        Socket client = null;
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        try {
            client = new Socket(host, port);

            // Set up streams for server I/O
            InputStream inFromServer = client.getInputStream();
            DataInputStream input = new DataInputStream(inFromServer);

            OutputStream outToServer = client.getOutputStream();
            DataOutputStream output = new DataOutputStream(outToServer);

            //Set up stream for keyboard entry
            Scanner userEntry = new Scanner(System.in);

            // System.out.println("Server says " + input.readUTF());
            System.out.println(input.readUTF());

            int service, input1, input2;
            do {
                System.out.print("\nservice: ");
                service = userEntry.nextInt();
                System.out.print("input1 : ");
                input1 = userEntry.nextInt();
                System.out.print("input2 : ");
                input2 = userEntry.nextInt();

                //send the numbers
                output.writeInt(service);
                output.writeInt(input1);
                output.writeInt(input2);

                // System.out.println("\nSERVER> " + input.readUTF());

                System.out.println(input.readUTF());

            } while (service != 0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NoSuchElementException ne){   //This exception may be raised when the server closes connection
            System.out.println("Connection closed");
        }
        finally {
            try {
                System.out.println("\n* Closing connection *");
                client.close();
            } catch (IOException ioEx) {
                System.out.println("Unable to disconnect");
                System.exit(1);
            }
        }
    }
}