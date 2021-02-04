
import com.sun.corba.se.spi.activation.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String name;

    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            doListen();
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong-SWW", e);
        }
    }

    public String getName() {
        return name;
    }

    private void doListen(){
        new Thread(() ->{
            try {
                doAuth();
                receivedMsg();
            }catch (Exception e){
                throw new RuntimeException("SWW",e);
            } finally {
                server.unsubscribe(this);
            }
        }).start();
    }

    private void doAuth() {
        try{
            while (true) {//бесконечно проверять
                String credentials = in.readUTF();
//            "n1@mail.com 1" samples
                if (credentials.startsWith("-auth")) {
                    String[] credentialValues = credentials.split("\\s");
                    server.getAuthenticationService()
                            .doAuth(credentialValues[1], credentialValues[2]) //
                            .ifPresent(
                                    user ->  {
                                        if (server.isLoggedIn(user.getNickname())) {
                                            sendMsg("cmd auth: Status ok");
                                            name = user.getNickname();
                                            server.broadcastMsg(name + " is logged in.");
                                            server.subscribe(this);
                                        }else {
                                            sendMsg("Current user is already logged in");
                                        }
                                    });
                } // успешная оперция

                new Runnable() {
                    @Override
                    public void run() {
                        sendMsg("No such user by email and passwords");
                    }
                };
                // если ничего не получится
            }
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong-SWW", e);
        }

    }
    private void receivedMsg(){
        try {
            while(true) {
                String msg = in.readUTF();
                if (msg.equals("-exit")) {
                    return;
                }
                server.broadcastMsg(msg);
            }
        }catch (IOException e){
            throw new RuntimeException("SWW", e);
        }

    }

    public void sendMsg (String msg){
        try{
            out.writeUTF(msg);
        }catch (IOException e){
            throw new RuntimeException("SWW", e);
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

