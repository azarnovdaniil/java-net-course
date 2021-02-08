package ru.uio.io;

import ru.uio.io.entity.User;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private User user;
    private AtomicBoolean isAuth;
    private String storePath;



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
        return user.getNickname();
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
                                    authUser -> {
                                        if (!server.isLoggedIn(authUser.getNickname())) {
                                            sendMessage("cmd auth: Status OK");
                                            user = authUser;
                                            storePath = user.getEmail().replace('@', '_');
                                            Path dir = Paths.get("store"+ File.separator + storePath);
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
            boolean file_close = false;
            boolean file_down_close = false;
            while (true) {
                byte[] initilize = new byte[1];
                in.read(initilize, 0, initilize.length);
                if (initilize[0] == 2) {
                    System.out.println("read = 2");
                    byte[] cmd_buff = new byte[3];
                    in.read(cmd_buff, 0, cmd_buff.length);
                    byte[] recv_data = Common.readStream(in);
                    switch (Integer.parseInt(new String(cmd_buff))) {
                        case 124://create new upload file in store (upload)
                            System.out.println(String.format("store/%s/%s", storePath, new String(recv_data)));
                            rw = new RandomAccessFile(String.format("store/%s/%s", storePath, new String(recv_data)), "rw");
                            System.out.println("124");
                            System.out.println(Files.exists(Paths.get(String.format("store/%s/%s", storePath, new String(recv_data)))));
                            out.write(Common.createDataPacket("125".getBytes("UTF8"), String.valueOf(current_file_pointer).getBytes("UTF8")));
                            out.flush();
                            break;
                        case 126://write data in file (upload)
                            System.out.println("126");
                            rw.seek(current_file_pointer);
                            rw.write(recv_data);
                            current_file_pointer = rw.getFilePointer();
                            System.out.println("Download percentage: " + ((float)current_file_pointer/rw.length())*100+"%");
                            out.write(Common.createDataPacket("125".getBytes("UTF8"), String.valueOf(current_file_pointer).getBytes("UTF8")));
                            out.flush();
                            break;
                        case 127://close file (upload)
                            if ("Close".equals(new String(recv_data))) {
                                file_close = true;
                            }
                            break;
                        case 120://close client
                            if ("exit".equals(new String(recv_data))){
                                loop_break = true;
                            }
                            break;
                        case 121://send list client file name
                            List<String> collect = Common.getFileList(String.format("store/%s/", storePath));
                            int i = 1;
                            for (String name : collect){
                                out.write(Common.createDataPacket("121".getBytes("UTF8"), String.format("%d. %s",i++,name).getBytes("UTF8")));
                                out.flush();
                            }
                            out.write(Common.createDataPacket("120".getBytes("UTF8"), "close".getBytes("UTF8")));
                            out.flush();
                            break;
                        case 125://send file data to client (download)
                            current_file_pointer = Long.valueOf(new String(recv_data));
                            int buff_len = (int) (rw.length() - current_file_pointer < 20000 ? rw.length() - current_file_pointer : 20000);
                            System.out.println("buff_len " + buff_len);
                            byte[] temp_buff = new byte[buff_len];
                            if (current_file_pointer != rw.length()) {
                                rw.seek(current_file_pointer);
                                rw.read(temp_buff, 0, temp_buff.length);
                                out.write(Common.createDataPacket("126".getBytes("UTF8"), temp_buff));
                                out.flush();
                                System.out.println("Upload percentage: " + ((float)current_file_pointer/rw.length())*100+"%");
                            } else {
                                file_down_close = true;
                            }
                            break;
                        case 115://open file in store and send name file (download)
                            List<String> list = Common.getFileList(String.format("store/%s/", storePath));
                            if(Integer.parseInt(new String(recv_data)) > list.size()){
                                out.write(Common.createDataPacket("119".getBytes("UTF8"), "File not found".getBytes("UTF8")));
                                out.flush();
                                break;
                            }
                            System.out.println(String.format("store/%s/%s", storePath, list.get(Integer.parseInt(new String(recv_data)) -1)));
                            rw = new RandomAccessFile(String.format("store/%s/%s", storePath, list.get(Integer.parseInt(new String(recv_data))-1)), "rw");
                            out.write(Common.createDataPacket("124".getBytes("UTF8"), list.get(Integer.parseInt(new String(recv_data))-1).getBytes("UTF8")));
                            out.flush();
                            break;
                    }
                }
                if (file_close == true) {
                    rw.close();
                    rw = null;
                    file_close = false;
//                    socket.close();
//                    return;
                }
                if (file_down_close == true) {
                    rw.close();
                    rw = null;
                    out.write(Common.createDataPacket("127".getBytes("UTF8"), "Close".getBytes("UTF8")));
                    out.flush();
                    file_down_close = false;
                }
                if (loop_break == true) {
                    return;
                }

            }
        } catch (IOException e) {
            throw new RuntimeException("SWW3", e);
        }
    }

    public void sendMessage(String message) {
        try {
            out.write(Common.createDataPacket("122".getBytes("UTF8"), message.getBytes("UTF8")));
        } catch (IOException e) {
            throw new RuntimeException("SWW4", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientHandler that = (ClientHandler) o;

        if (!Objects.equals(server, that.server)) return false;
        if (!Objects.equals(socket, that.socket)) return false;
        if (!Objects.equals(in, that.in)) return false;
        if (!Objects.equals(out, that.out)) return false;
        if (!Objects.equals(user, that.user)) return false;
        if (!Objects.equals(isAuth, that.isAuth)) return false;
        return Objects.equals(storePath, that.storePath);
    }

    @Override
    public int hashCode() {
        int result = server != null ? server.hashCode() : 0;
        result = 31 * result + (socket != null ? socket.hashCode() : 0);
        result = 31 * result + (in != null ? in.hashCode() : 0);
        result = 31 * result + (out != null ? out.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (isAuth != null ? isAuth.hashCode() : 0);
        result = 31 * result + (storePath != null ? storePath.hashCode() : 0);
        return result;
    }
}
