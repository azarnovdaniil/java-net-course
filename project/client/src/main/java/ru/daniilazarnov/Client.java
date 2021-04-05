package ru.daniilazarnov;

import helpers.ConfigHelper;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import messages.Message;
import messages.MessageType;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.util.Scanner;

public class Client {

    public static final int DEFAULT_PORT = 9876;
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final String CONFIG_FILE = "client.config";

    private Socket socket;
    private DataOutputStream out;
    private Scanner in;
    private CredentialsEntry user;
    private ConfigHelper config;

    public static void main(String[] args) {
        try {
            new Client().run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Client() throws IOException {
        init();
    }

    private void init() {
        try {
            config = new ConfigHelper(Path.of(CONFIG_FILE));
            config.load();

            this.socket = new Socket(config.getProperty("app.host"), Integer.parseInt(config.getProperty("app.port")));
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new Scanner(socket.getInputStream());

        } catch (FileNotFoundException e) {
            System.out.println("не найден файл настроек будет создан новый со стандартными настройками");

            try {
                this.socket = new Socket(InetAddress.getByName(DEFAULT_HOST), DEFAULT_PORT);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            try {
                this.out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            try {
                this.in = new Scanner(socket.getInputStream());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            //todo save to config

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run() {
        //todo properly with threads
        auth("l1", "p1", "u1");
    }

    //todo use config
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

    public boolean register(String login, String password, String username) {
        boolean res = false;

        //todo registration
        //todo savings to config

        return res;
    }
}
