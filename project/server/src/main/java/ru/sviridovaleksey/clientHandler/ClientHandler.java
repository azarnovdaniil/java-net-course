package ru.sviridovaleksey.clientHandler;

import ru.sviridovaleksey.Command;
import ru.sviridovaleksey.commands.MessageCommandData;
import ru.sviridovaleksey.newClientConnection.ClientConnection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class ClientHandler {

    private ClientConnection clientConnection;
    private SocketChannel clientSocket;
    ObjectInputStream in;


    public ClientHandler(ClientConnection clientConnection, SocketChannel clientSocket) {
        this.clientConnection = clientConnection;
        this.clientSocket = clientSocket;

    }

//    public void handle() {
//        while (true) {
//            readMessage();
//        }
//    }

}
//    public void readMessage ()  {
//        try {
//            in = new ObjectInputStream(clientSocket.socket().getInputStream());
//            Command command = (Command) in.readObject();
//            MessageCommandData messageCommandData = (MessageCommandData) command.getData();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//}

//    public void readMessage () throws IOException {
//        ByteBuffer buf = ByteBuffer.allocate(1024);
//        int bytesRead = clientSocket.read(buf);
//        while (bytesRead != -1) {
//            buf.flip();
//            while(buf.hasRemaining()){
//                System.out.print((char) buf.get());
//            }
//            buf.clear();
//            bytesRead = clientSocket.read(buf);
//        }
//
//
//    }
