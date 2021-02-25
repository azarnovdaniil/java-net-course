package server.Services;
import common.makeCommand;
import common.Services.MessageCommandService;
import server.Services.ClientConnection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.SocketChannel;
public class ClientHandler
{
    private ClientConnection clientConnection;
    private SocketChannel clientSocket;
    ObjectInputStream in;


    public ClientHandler(ClientConnection clientConnection, SocketChannel clientSocket) {
        this.clientConnection = clientConnection;
        this.clientSocket = clientSocket;

    }

    public void handle() {
        while (true) {
            readMessage();
        }
    }

    public void readMessage ()  {
        try {
            in = new ObjectInputStream(clientSocket.socket().getInputStream());
            makeCommand command = (makeCommand) in.readObject();
            MessageCommandService messageCommandData = (MessageCommandService) command.getData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
