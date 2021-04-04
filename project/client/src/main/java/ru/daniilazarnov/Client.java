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
        this.in = new Scanner(socket.getInputStream());
    }

    private void run() {
        //todo
        auth("l1", "p1", "u1");
    }

    public void auth(String login, String password, String username) {

        try (ObjectEncoderOutputStream oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream odis = new ObjectDecoderInputStream(socket.getInputStream())) {

            CredentialsEntry user = new CredentialsEntry(login, password, username);
            Message message = new Message(MessageType.AUTHORIZATION, user);

            oeos.writeObject(message);
            oeos.flush();

            String answer = (String) odis.readObject();

            if (!answer.equals("ok")) {
                throw new ClientConnectionException(answer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(String path) {
        //todo
        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream odis = new ObjectDecoderInputStream(socket.getInputStream())) {

            out.writeObject("/upload");

            String answer = (String) odis.readObject();

            if (!answer.equals("ok")) {
                throw new ClientConnectionException(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(String filename) {
        //todo
        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream())) {

            out.writeObject("/download " + filename);

            //todo проверка что все ок
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void renameFile(String oldName, String newName) {
        //todo
        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream())) {

            out.writeObject("/rename " + oldName + " " + newName);

            //todo проверка что все ок
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFile(String fileName) {
        //todo
        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream())) {

            out.writeObject("/remove " + fileName);

            //todo проверка что все ок
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listFiles() {
        //todo
        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream())) {

            out.writeObject("/list");

            //todo проверка что все ок
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
