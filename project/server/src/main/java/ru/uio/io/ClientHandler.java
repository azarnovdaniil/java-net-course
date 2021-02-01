package ru.uio.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String name;
    private AtomicBoolean isAuth;


    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            isAuth = new AtomicBoolean();
            isAuth.set(false);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(!isAuth.get()){
                        try {
                            sendMessage("-exit");
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 120000);

            doListen();
        } catch (IOException e) {
            throw new RuntimeException("SWW8", e);
        }
    }

    public String getName() {
        return name;
    }

    private void doListen() {
        new Thread(() -> {
            try {
                doAuth();
                receiveMessage();
            } catch (Exception e) {
                throw new RuntimeException("SWW1", e);
            } finally {
                server.unsubscribe(this);
            }
        }).start();
    }

    private void doAuth() {
        try {
            while (true) {
                String credentials = in.readUTF();
                /**
                 * Input credentials sample
                 * "-auth n1@mail.com 1"
                 */
                if (credentials.startsWith("-auth")) {
                    /**
                     * After splitting sample
                     * array of ["-auth", "n1@mail.com", "1"]
                     */

                    String[] credentialValues = credentials.split("\\s");
                    server.getAuthenticationService()
                            .doAuth(credentialValues[1], credentialValues[2])
                            .ifPresentOrElse(
                                    user -> {
                                        if (!server.isLoggedIn(user.getNickname())) {
                                            sendMessage("cmd auth: Status OK");
                                            name = user.getNickname();
                                            Path dir = Paths.get("store/" + name);
                                            try {
                                                if(!Files.isDirectory(dir))
                                                    Files.createDirectory(dir);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            //server.broadcastMessage(name + " is logged in.");
                                            server.subscribe(this);
                                            isAuth.set(true);
                                        } else {
                                            sendMessage("Current user is already logged in.");
                                        }
                                    },
                                    () -> sendMessage("No a such user by email and password.")
                            );
                    if (isAuth.get()){
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("SWW2", e);
        }
    }

    /**
     * Receives input data from {@link ClientHandler#in} and then broadcast via {@link Server#broadcastMessage(String)}
     */
    private void receiveMessage() {
        try {
            RandomAccessFile rw = null;
            long current_file_pointer = 0;
            boolean loop_break = false;
            while (true) {
                byte[] initilize = new byte[1];
                in.read(initilize, 0, initilize.length);
                if (initilize[0] == 2) {
                    System.out.println("read = 2");
                    byte[] cmd_buff = new byte[3];
                    in.read(cmd_buff, 0, cmd_buff.length);
                    byte[] recv_data = Common.readStream(in);
                    switch (Integer.parseInt(new String(cmd_buff))) {
                        case 124:
                            System.out.println(String.format("store/%s/%s", name, new String(recv_data)));
                            rw = new RandomAccessFile(String.format("store/%s/%s", name, new String(recv_data)), "rw");
                            System.out.println("124");
                            System.out.println(Files.exists(Paths.get(String.format("store/%s/%s", name, new String(recv_data)))));
                            out.write(Common.createDataPacket("125".getBytes("UTF8"), String.valueOf(current_file_pointer).getBytes("UTF8")));
                            out.flush();
                            break;
                        case 126:
                            System.out.println("126");
                            rw.seek(current_file_pointer);
                            rw.write(recv_data);
                            current_file_pointer = rw.getFilePointer();
                            System.out.println("Download percentage: " + ((float)current_file_pointer/rw.length())*100+"%");
                            out.write(Common.createDataPacket("125".getBytes("UTF8"), String.valueOf(current_file_pointer).getBytes("UTF8")));
                            out.flush();
                            break;
                        case 127:
                            if ("Close".equals(new String(recv_data))) {
                                loop_break = true;
                            }
                            break;
                    }
                }
                if (loop_break == true) {
                    rw.close();
                    rw = null;
//                    socket.close();
//                    return;
                }
                String message = in.readUTF();
                if (message.equals("-exit")) {
                    return;
                }//
//
                //server.broadcastMessage(message);
            }
        } catch (IOException e) {
            throw new RuntimeException("SWW3", e);
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException("SWW4", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientHandler that = (ClientHandler) o;
        return Objects.equals(server, that.server) &&
                Objects.equals(socket, that.socket) &&
                Objects.equals(in, that.in) &&
                Objects.equals(out, that.out) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(server, socket, in, out, name);
    }
}
