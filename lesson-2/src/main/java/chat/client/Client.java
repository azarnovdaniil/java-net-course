package chat.client;

import chat.helpers.FileHelper;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public Client(String host, int port) {
        try {
            socket = new Socket(host, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            throw new ClientConnectionException("SWW", e);
        }
    }

    public String receiveMessage() throws ClientConnectionException {
        try {
            return in.readUTF();
        } catch (IOException e) {
            throw new ClientConnectionException("SWW", e);
        }
    }

    public void sendMessage(String message) throws ClientConnectionException {
        try {
            out.writeUTF(message);
            //1
            FileHelper.writeHistory(message);
        } catch (IOException e) {
            throw new ClientConnectionException("SWW", e);
        }
    }

    public void close() {
        close(in);
        close(out);
    }

    private void close(Closeable stream) {
        if (stream == null) {
            return;
        }

        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
