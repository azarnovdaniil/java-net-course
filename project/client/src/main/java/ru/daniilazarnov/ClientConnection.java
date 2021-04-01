package ru.daniilazarnov;

import ru.daniilazarnov.commands.ArgumentsForCommand;
import ru.daniilazarnov.commands.FabricOfCommands;
import ru.daniilazarnov.commands.ICommand;

import javax.annotation.processing.FilerException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;
import java.util.Scanner;

public class ClientConnection {
    private SocketChannel clientSocketChannel;
    private Selector selector;
    private final String serverHost;
    private final int serverPort;
    private final Path resourcesPath = Paths.get("project/client/src/main/resources");
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8199;

    public ClientConnection(String host, int port) {
        this.serverHost = host;
        this.serverPort = port;
    }
    public ClientConnection() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public SocketChannel getClientSocketChannel() {
        return clientSocketChannel;
    }

    public Selector getSelector() {
        return selector;
    }


    public void connect() throws IOException {
        clientSocketChannel = SocketChannel.open(new InetSocketAddress(serverHost, serverPort));
        clientSocketChannel.configureBlocking(false);
        selector = Selector.open();
        clientSocketChannel.register(selector, SelectionKey.OP_READ);
        handleReadFromServer();
    }

    public void handleReadFromServer() {
        new Thread(() -> {
            try {
                SelectionKey key = null;
                while (clientSocketChannel.isOpen()) {
                    selector.select();
                    Iterator<SelectionKey> iterator = selector.keys().iterator();
                    while (iterator.hasNext()) {
                        key = iterator.next();
                        if (key.isReadable()) {
                            receiveBytesFromServer(key);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void receiveBytesFromServer(SelectionKey key) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        clientSocketChannel.read(byteBuffer);
        byteBuffer.flip();
        Optional<Commands> command = Commands.getCommand(byteBuffer.get());
        switch (command.get()) {
            case message:
                receiveStringServer(key);
                break;
            case stor:
                receiveFileFromServer(key);
                break;
            default:
                throw new FilerException("Ошибка передачи данных");
        }
    }

    public void receiveFileFromServer(SelectionKey key) throws IOException {
        String fileName = Protocol.getStringFromSocketChannel(clientSocketChannel);
        Path targetPath = resourcesPath.resolve(fileName);
        ByteBuffer byteBufferForFile = Protocol.getFileInByteBufferFromSocketChannel(key);
        try {
            Files.write(targetPath, byteBufferForFile.array());
            System.out.print("File download successfully\n>");
        } catch (IOException e) {
            System.out.println("File transfer error: " + e.toString());
        }
    }

    private void receiveStringServer(SelectionKey key) throws IOException {
        String currentDir = Protocol.getStringFromSocketChannel((SocketChannel) key.channel());
        System.out.println(currentDir);
        System.out.print("> ");
    }

    public void commandInterpretationFromClient(String message) throws IOException {
        String[] splitMessage = message.split(" ");
        ICommand command = FabricOfCommands.getCommand(ArgumentsForCommand.getArguments(splitMessage));
        command.apply(this);
    }


    public static void main(String[] args) throws IOException, URISyntaxException {
        ClientConnection clientConnection = new ClientConnection();
        clientConnection.connect();
        String message = "";
        do {
            Scanner scanner = new Scanner(System.in);
            message = scanner.nextLine();
            clientConnection.commandInterpretationFromClient(message);
        } while (!message.equals("disconnect"));
            clientConnection.getClientSocketChannel().close();
    }
}
