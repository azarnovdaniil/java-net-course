package ru.daniilazarnov.clientConnection;

import ru.daniilazarnov.Commands;
import ru.daniilazarnov.Protocol;
import ru.daniilazarnov.clientConnection.commands.ArgumentsForCommand;
import ru.daniilazarnov.clientConnection.commands.FabricOfCommands;
import ru.daniilazarnov.clientConnection.commands.ICommand;

import javax.annotation.processing.FilerException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;

public class Connection implements IClientConnection {
    private SocketChannel socketChannel;
    private Selector selector;
    private final String serverHost;
    private final int serverPort;
    private final Path resourcesPath = Paths.get("project/client/src/main/resources");
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8199;

    public Connection(String host, int port) {
        this.serverHost = host;
        this.serverPort = port;
    }
    public Connection() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    @Override
    public boolean connect() {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(serverHost, serverPort));
            socketChannel.configureBlocking(false);
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_READ);
            handleReadFromServer();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean disconnect() {
        try {
            ArgumentsForCommand arguments = new ArgumentsForCommand(Commands.disconnect, null);
            ICommand command = FabricOfCommands.getCommand(arguments);
            command.apply(socketChannel);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean listOfFileDirectory() {
        try {
            ArgumentsForCommand arguments = new ArgumentsForCommand(Commands.ls, null);
            ICommand command = FabricOfCommands.getCommand(arguments);
            command.apply(socketChannel);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean presentWorkDirectory() {
        try {
            ArgumentsForCommand arguments = new ArgumentsForCommand(Commands.pwd, null);
            ICommand command = FabricOfCommands.getCommand(arguments);
            command.apply(socketChannel);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean showHelp() {
        try {
            ArgumentsForCommand arguments = new ArgumentsForCommand(Commands.help, null);
            ICommand command = FabricOfCommands.getCommand(arguments);
            command.apply(socketChannel);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean authorization(String userName, String password) {
        try {
            ArgumentsForCommand arguments = new ArgumentsForCommand(Commands.user, new String[]{userName});
            ICommand command = FabricOfCommands.getCommand(arguments);
            command.apply(socketChannel);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean changeDirectory(Path targetDir) {
        try {
            ArgumentsForCommand arguments = new ArgumentsForCommand(Commands.cd, new String[]{targetDir.toString()});
            ICommand command = FabricOfCommands.getCommand(arguments);
            command.apply(socketChannel);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean downloadFile(String fileName) {
        try {
            ArgumentsForCommand arguments = new ArgumentsForCommand(Commands.retr, new String[]{fileName});
            ICommand command = FabricOfCommands.getCommand(arguments);
            command.apply(socketChannel);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean makeDirectory(String dirName) {
        try {
            ArgumentsForCommand arguments = new ArgumentsForCommand(Commands.mkd, new String[]{dirName});
            ICommand command = FabricOfCommands.getCommand(arguments);
            command.apply(socketChannel);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean uploadFile(Path pathToFile) {
        try {
            ArgumentsForCommand arguments = new ArgumentsForCommand(Commands.stor, new String[]{pathToFile.toString()});
            ICommand command = FabricOfCommands.getCommand(arguments);
            command.apply(socketChannel);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void handleReadFromServer() {
        new Thread(() -> {
            try {
                SelectionKey key = null;
                while (socketChannel.isOpen()) {
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
        socketChannel.read(byteBuffer);
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
        String fileName = Protocol.getStringFromSocketChannel(socketChannel);
        Path targetPath = resourcesPath.resolve(fileName);
        ByteBuffer byteBufferForFile = Protocol.getFileInByteBufferFromSocketChannel(key);
        try {
            Files.write(targetPath, byteBufferForFile.array());
            System.out.print("File download successfully\n>");
        } catch (IOException e) {
            System.out.println("File transfer error: " + e.toString() + "\n>");
        }
    }

    private void receiveStringServer(SelectionKey key) throws IOException {
        String currentDir = Protocol.getStringFromSocketChannel((SocketChannel) key.channel());
        System.out.println(currentDir);
        System.out.print("> ");
    }

//    public void commandInterpretationFromClient(String message) throws IOException {
//        String[] splitMessage = message.split(" ");
//        ICommand command = FabricOfCommands.getCommand(ArgumentsForCommand.getArguments(splitMessage));
//        command.apply(this);
//    }
}
