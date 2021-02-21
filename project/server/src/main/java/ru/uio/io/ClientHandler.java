package ru.uio.io;

import ru.uio.io.auth.Registration;
import ru.uio.io.commands.Command;
import ru.uio.io.commands.CreateNewFileCommand;
import ru.uio.io.entity.User;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
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

                    if (credentialValues.length < 3) {
                        sendMessage("wrong login or pass");
                        continue;
                    }
                    server.getAuthenticationService()
                            .doAuth(credentialValues[1], credentialValues[2])
                            .ifPresentOrElse(
                                    authUser -> {
                                        if (!server.isLoggedIn(authUser.getNickname())) {
                                            sendMessage("cmd auth: Status OK");
                                            user = authUser;
                                            storePath = user.getEmail().replace('@', '_');
                                            Path dir = Paths.get("store" + File.separator + storePath);
                                            try {
                                                if (!Files.isDirectory(dir))
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
                    if (isAuth.get()) {
                        break;
                    }
                }
                if (credentials.startsWith("-reg")) {
                    String[] credentialValues = credentials.split("\\s");

                    if (credentialValues.length < 3) {
                        sendMessage("wrong login or pass");
                        continue;
                    }
                    Registration reg = new Registration(server.getConnect());
                    boolean res = reg.reg(credentialValues[1], credentialValues[2]);

                    if (res) {
                        sendMessage("reg success");
                    } else {
                        sendMessage("reg failed");
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
            long currentFilePointer = 0;
            boolean loopBreak = false;
            boolean fileClose = false;
            boolean fileDownClose = false;
            while (true) {
                byte[] initilize = new byte[1];
                in.read(initilize, 0, initilize.length);
                if (initilize[0] == 2) {
                    byte[] cmdBuff = new byte[3];
                    in.read(cmdBuff, 0, cmdBuff.length);
                    byte[] recvData = Common.readStream(in);
                    switch (Integer.parseInt(new String(cmdBuff))) {
                        case 124://create new upload file in store (upload)
                            Command command = new CreateNewFileCommand(in, out, storePath, recvData);
                            command.execute();
                            break;
                        case 120://close client
                            if ("exit".equals(new String(recvData))) {
                                loopBreak = true;
                            }
                            break;
                        case 121://send list client file name
                            List<String> collect = Common.getFileList(String.format("store/%s/", storePath));
                            int i = 1;
                            for (String name : collect) {
                                out.write(Common.createDataPacket("121".getBytes(StandardCharsets.UTF_8), String.format("%d. %s", i++, name).getBytes(StandardCharsets.UTF_8)));
                                out.flush();
                            }
                            out.write(Common.createDataPacket("120".getBytes(StandardCharsets.UTF_8), "close".getBytes(StandardCharsets.UTF_8)));
                            out.flush();
                            break;
                        case 125://send file data to client (download)
                            currentFilePointer = Long.parseLong(new String(recvData));
                            int buff_len = (int) (rw.length() - currentFilePointer < 20000 ? rw.length() - currentFilePointer : 20000);
                            System.out.println("buff_len " + buff_len);
                            byte[] temp_buff = new byte[buff_len];
                            if (currentFilePointer != rw.length()) {
                                rw.seek(currentFilePointer);
                                rw.read(temp_buff, 0, temp_buff.length);
                                out.write(Common.createDataPacket("126".getBytes(StandardCharsets.UTF_8), temp_buff));
                                out.flush();
                                System.out.println("Upload percentage: " + ((float) currentFilePointer / rw.length()) * 100 + "%");
                            } else {
                                fileDownClose = true;
                            }
                            break;
                        case 115://open file in store and send name file (download)
                        {
                            List<String> list = Common.getFileList(String.format("store/%s/", storePath));
                            if (Integer.parseInt(new String(recvData)) > list.size()) {
                                out.write(Common.createDataPacket("119".getBytes(StandardCharsets.UTF_8), "File not found".getBytes(StandardCharsets.UTF_8)));
                                out.flush();
                                break;
                            }
                            System.out.println(String.format("store/%s/%s", storePath, list.get(Integer.parseInt(new String(recvData)) - 1)));
                            rw = new RandomAccessFile(String.format("store/%s/%s", storePath, list.get(Integer.parseInt(new String(recvData)) - 1)), "rw");
                            out.write(Common.createDataPacket("124".getBytes(StandardCharsets.UTF_8), list.get(Integer.parseInt(new String(recvData)) - 1).getBytes(StandardCharsets.UTF_8)));
                            out.flush();
                            break;
                        }
                        case 116://delete file
                            List<String> list = Common.getFileList(String.format("store/%s/", storePath));
                            if (Integer.parseInt(new String(recvData)) > list.size()) {
                                out.write(Common.createDataPacket("119".getBytes(StandardCharsets.UTF_8), "File not found".getBytes(StandardCharsets.UTF_8)));
                                out.flush();
                                break;
                            }
                            String fileName = String.format("store/%s/%s", storePath, list.get(Integer.parseInt(new String(recvData)) - 1));
                            System.out.println(fileName);
                            boolean result = Files.deleteIfExists(Paths.get(fileName));
                            if (result){
                                out.write(Common.createDataPacket("119".getBytes(StandardCharsets.UTF_8), "File delete".getBytes(StandardCharsets.UTF_8)));
                                out.flush();
                                break;
                            }
                            break;
                    }
                }
                if (fileClose == true) {
                    rw.close();
                    rw = null;
                    fileClose = false;
//                    socket.close();
//                    return;
                }
                if (fileDownClose == true) {
                    rw.close();
                    rw = null;
                    out.write(Common.createDataPacket("127".getBytes(StandardCharsets.UTF_8), "Close".getBytes(StandardCharsets.UTF_8)));
                    out.flush();
                    fileDownClose = false;
                }
                if (loopBreak == true) {
                    return;
                }

            }
        } catch (IOException e) {
            throw new RuntimeException("SWW3", e);
        }
    }

    public void sendMessage(String message) {
        try {
            out.write(Common.createDataPacket("122".getBytes(StandardCharsets.UTF_8), message.getBytes(StandardCharsets.UTF_8)));
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

    private void executeCommand(Command command) {
        command.execute();
    }

    public DataInputStream getIn() {
        return in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public User getUser() {
        return user;
    }

    public String getStorePath() {
        return storePath;
    }
}
