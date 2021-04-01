package ru.daniilazarnov;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
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
        auth("l1", "p1");
    }

    public boolean auth(String username, String password) {
        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream());
                ObjectDecoderInputStream in = new ObjectDecoderInputStream(socket.getInputStream())) {

            CredentialsEntry user = new CredentialsEntry("l1", "p1", "u1");
            out.writeObject("/auth");
            out.flush();

            out.writeObject(user);
            out.flush();

            String answer = (String) in.readObject();

            if (!answer.equals("ok")) {
                throw new Exception(answer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void uploadFile(String path) {
        //todo
        try (ObjectEncoderOutputStream out = new ObjectEncoderOutputStream(socket.getOutputStream())) {

            out.writeObject("/upload");

            //todo проверка что все ок
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
