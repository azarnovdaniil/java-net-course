package ru.daniilazarnov;

import helpers.ConfigHelper;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import messages.Message;
import messages.MessageType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Client {

    public static final int DEFAULT_PORT = 9876;
    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final String CONFIG_FILE = "client.config";
    public static final String CONFIG_NOT_FOUND = "не найден файл настроек будет создан новый.";

    private Socket socket;
    private CredentialsEntry user;
    private ConfigHelper config;

    public Client() throws IOException {
        init();
    }

    public CredentialsEntry getUser() {
        return user;
    }

    public ConfigHelper getConfig() {
        return config;
    }

    public Socket getSocket() {
        return socket;
    }

    private void init() {
        try {
            config = new ConfigHelper(Path.of(CONFIG_FILE));
            config.load();

            this.socket = new Socket(config.getProperty("app.host"), Integer.parseInt(config.getProperty("app.port")));

        } catch (FileNotFoundException e) {
            System.out.println(CONFIG_NOT_FOUND);

            try {
                this.socket = new Socket(InetAddress.getByName(DEFAULT_HOST), DEFAULT_PORT);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            List<String> strings = new ArrayList<>();
            strings.add("app.host=" + DEFAULT_HOST);
            strings.add("app.port=" + DEFAULT_PORT);

            try {
                Files.write(Path.of(CONFIG_FILE), strings, StandardOpenOption.WRITE);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void run(InputStream stream) {
        new Thread(new InputHandler(this, stream)).start();

    }

    public void auth(String login, String password, String username) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void register(String login, String password, String username) {
        try (ObjectEncoderOutputStream oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
             ObjectDecoderInputStream odis = new ObjectDecoderInputStream(socket.getInputStream())) {

            user = new CredentialsEntry(login, password, username);
            Message message = new Message(MessageType.REGISTRATION, user);

            oeos.writeObject(message);
            oeos.flush();

            String answer = (String) odis.readObject();

            if (!answer.equals("ok")) {
                throw new ClientConnectionException(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> strings = new ArrayList<>();
        strings.add("app.login=" + login);
        strings.add("app.password=" + password);
        strings.add("app.nickname=" + username);

        try {
            Files.write(Path.of(CONFIG_FILE), strings, StandardOpenOption.APPEND);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
