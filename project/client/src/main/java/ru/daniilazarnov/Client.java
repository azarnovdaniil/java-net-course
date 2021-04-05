package ru.daniilazarnov;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import messages.Message;
import messages.MessageType;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static final int PORT = 8189;

    private Socket socket;
    private DataOutputStream out;
    private Scanner in;
    private CredentialsEntry user;

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
        this.in = new Scanner(socket.getInputStream());
    }

    private void run() {
        //todo
        auth("l1", "p1", "u1");
    }

    public boolean auth(String login, String password, String username) {
        boolean res = false;

        try (ObjectEncoderOutputStream oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream odis = new ObjectDecoderInputStream(socket.getInputStream())) {

            user = new CredentialsEntry(login, password, username);
            Message message = new Message(MessageType.AUTHORIZATION, user);

            oeos.writeObject(message);
            oeos.flush();

            String answer = (String) odis.readObject();

            if (!answer.equals("ok")) {
                throw new ClientConnectionException(answer);
            }

            res = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }


}
