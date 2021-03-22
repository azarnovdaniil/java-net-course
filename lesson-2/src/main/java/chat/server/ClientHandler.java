package chat.server;

import chat.helpers.DataBaseHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import static chat.helpers.FileHelper.saveFile;

public class ClientHandler {
    private String name;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private Chat chat;
    private boolean isLoggedIn;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public ClientHandler(Socket socket, Chat chat) {
        this.socket = socket;
        this.chat = chat;
        this.isLoggedIn = false;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("SWW", e);
        }

        listen();
    }

    public String getName() {
        return name;
    }

    private void listen() {
        logger.log(Level.INFO, "ClientHandler is listening");
        new Thread(() -> {
            if (!isLoggedIn) doAuth();
            receiveMessage();
        }).start();
    }

    private void doAuth() {
        sendMessage("Please enter credentials. Sample [-auth login password] \n " +
                "or you will be disconnected after 120 seconds");
        try {
            /* ", de
             * -auth login password
             * sample: -auth l1 p1
             */
            socket.setSoTimeout(120_000);

            while (true) {
                String mayBeCredentials = in.readUTF();
                if (mayBeCredentials.startsWith("-auth")) {
                    String[] credentials = mayBeCredentials.split("\\s");
                    String mayBeNickname = chat.getAuthenticationService()
                            .findNicknameByLoginAndPassword(credentials[1], credentials[2]);
                    if (mayBeNickname != null) {
                        if (!chat.isNicknameOccupied(mayBeNickname)) {
                            sendMessage("[INFO] Auth OK");
                            name = mayBeNickname;
                            chat.broadcastMessage(String.format("[%s] logged in", name));
                            chat.subscribe(this);
                            isLoggedIn = true;
                            socket.setSoTimeout(0);
                            logger.log(Level.INFO, "logged in - " + name);

                            return;
                        } else {
                            sendMessage("[INFO] Current user is already logged in.");
                        }
                    } else {
                        sendMessage("[INFO] Wrong login or password.");
                    }
                }
            }
        } catch (SocketTimeoutException | SocketException ex) {
            sendMessage("you was disconnected due to unauthorized activity");
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
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            if (!socket.isClosed()) {
                out.writeUTF(message);
            }
        } catch (IOException e) {
            throw new RuntimeException("SWW", e);
        }
    }

    public void receiveMessage() {
        while (true) {
            try {
                if (!socket.isClosed()) {
                    String message = in.readUTF();
                    if (message.startsWith("-exit")) {
                        chat.unsubscribe(this);
                        chat.broadcastMessage(String.format("[%s] logged out", name));
                        break;
                    }
                    //
                    if (message.startsWith("-rename")) {
                        String[] data = message.split("\\s");
                        if (!chat.isNicknameOccupied(data[2])) {
                            if (DataBaseHelper.updateNickname(data[1], data[2])) {
                                chat.getAuthenticationService().update();
                                name = data[2];
                                sendMessage("[INFO] you was successfully renamed");
                            }
                        } else {
                            sendMessage("[INFO] This nickname is already ocupped");
                        }
                        break;
                    }
                    //
                    if(message.startsWith("-file")){
                        String[] data = message.split("\\s");
                        try {
                            saveFile(socket.getInputStream(), Path.of(data[1]));
                            sendMessage("[INFO] file saved properly");
                        } catch (IOException e) {
                            sendMessage("[INFO] Can't save file/ Error is: " + e.getMessage());
                        }
                    }

                    //
                    chat.broadcastMessage(String.format("[%s]: %s", name, message));
                }
            } catch (IOException e) {
                throw new RuntimeException("SWW", e);
            }
        }
    }
}
