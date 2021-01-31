package ru.sviridovaleksey.network;

import org.apache.commons.lang3.SerializationUtils;
import ru.sviridovaleksey.Command;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Connection {

    private String serverAddress;
    private int usePort;
    private SocketChannel SocketChannel;
    private SocketChannel clientSocket;
    private Selector selector;
    private int bufCapacity = 1024;


    public Connection(String serverAddress, int usePort) {
        this.serverAddress = serverAddress;
        this.usePort = usePort;

    }

    public void openConnection() {
        try {
            SocketChannel = SocketChannel.open();
            SocketChannel.connect(new InetSocketAddress(serverAddress, usePort));
            selector = Selector.open();

            while (SocketChannel.isOpen()) {
                System.out.println("введите сообщение:");
                Scanner scanner = new Scanner(System.in);
                String message = scanner.next();
                Command command = Command.message("User1", message);
                writeMessage(command);
                command = Command.createNewDirectory("NEWDIRECTORY", "novuiFile");
                writeMessage(command);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeMessage (Command command) throws IOException {

        ByteBuffer buf = ByteBuffer.allocate(bufCapacity);
        buf.clear();
        byte[] by =SerializationUtils.serialize(command);
        buf.put(by);
        buf.flip();
        while (buf.hasRemaining()) {
            SocketChannel.write(buf);
        }
        buf.clear();

    }

}
