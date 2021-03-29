package ru.daniilazarnov;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class FileProtocol {
    private static final ByteBuffer serverByteBuffer = ByteBuffer.allocate(8192);
    private static final int DEFAULT_BYTE_BUFFER_SIZE = 8192;
    private static Path rootDir;
    private static SocketChannel clientSocketChannel;
    private static SelectionKey key;
    private static String serverHost;
    private static int serverPort;
    private static ByteBuffer clientByteBuffer;
    private static Selector selector;



    ///////////////////////////////////////
//    Клиентская часть

    public static void connect(InetSocketAddress inetSocketAddress) throws IOException {
        clientSocketChannel = SocketChannel.open(inetSocketAddress);
        clientSocketChannel.configureBlocking(false);
        selector = Selector.open();
        clientSocketChannel.register(selector, SelectionKey.OP_READ);
        handleReadFromServer();
    }

    public static void commandInterpretationFromClient(String message) throws IOException {
        String[] splitMessage = message.split(" ");
        splitMessage[0] = splitMessage[0].toLowerCase();
        int fileNameLength;
        String filename;

        switch (splitMessage[0]) {
            case "mkd":
                makeDir(splitMessage);
                break;
            case "connect":
                connectToServer(splitMessage);
                break;
            case "stor":
                sendFileToServer(splitMessage);
                break;
            case "retr":
                //передача с сервера на клиент
                receiveFileFromServer(splitMessage);
                break;
            case "user":
                sendAuth(splitMessage);
                break;
            case "cd":
                changeDir(splitMessage);
                break;
            case "help":
                showClientHelp();
                break;
            default:
                System.out.print("Unknown command\n> ");
        }
    }

    public static void showClientHelp() {
        System.out.print("user <name> - authorization\n" +
                        "stor <file location> - uploading a file to the server\n" +
                        "retr <file name> - downloading a file from the server\n" +
                        "mkd <directory name/path to directory> - creating a directory\n" +
                        "cd <path to directory> - change directory");
    }

    public static void setRootDir(String dir) {
        rootDir = Paths.get(dir);
    }

    private static void makeDir(String[] splitMessage) throws IOException {
        if (clientSocketChannel == null) {
            return;
        }
        if (splitMessage.length != 2) {
            System.out.println("Wrong command");
            return;
        }
        clientByteBuffer = ByteBuffer.allocate(5 + splitMessage[1].length());
        clientByteBuffer.put((byte) 2);
        clientByteBuffer.putInt(splitMessage[1].length());
        clientByteBuffer.put(splitMessage[1].getBytes());
        clientByteBuffer.flip();
        clientSocketChannel.write(clientByteBuffer);
    }

    private static void connectToServer(String[] splitMessage) throws IOException {
        if (splitMessage.length != 3) {
            System.out.println("Wrong command");
            return;
        }
        serverHost = splitMessage[1];
        serverHost = splitMessage[2];
        clientSocketChannel = SocketChannel.open(new InetSocketAddress(serverHost, serverPort));
        clientSocketChannel.configureBlocking(false);
        selector = Selector.open();
        clientSocketChannel.register(selector, SelectionKey.OP_READ);
        handleReadFromServer();
    }

    private static void sendFileToServer(String[] splitMessage) throws IOException {
        int fileNameLength;
        String filename;
        if (clientSocketChannel == null) {
            return;
        }
        if (splitMessage.length != 2) {
            System.out.println("Wrong command");
            return;
        }
        clientByteBuffer = ByteBuffer.allocate(DEFAULT_BYTE_BUFFER_SIZE);
        Path pathSrcFile = Paths.get(splitMessage[1]);
        if (!Files.exists(pathSrcFile)) {
            return;
        }
        clientByteBuffer.put((byte) 1);
        filename = pathSrcFile.getFileName().toString();
        fileNameLength = filename.length();
        clientByteBuffer.putInt(fileNameLength);
        clientByteBuffer.put(filename.getBytes());
        clientByteBuffer.putInt((int) Files.size(pathSrcFile));
        FileChannel srcFileChannel = (FileChannel) Files.newByteChannel(pathSrcFile);
        while (srcFileChannel.read(clientByteBuffer) != 0) {
            clientByteBuffer.flip();
            clientSocketChannel.write(clientByteBuffer);
            clientByteBuffer.clear();
        }
        srcFileChannel.close();
    }

    private static void receiveFileFromServer(String[] splitMessage) throws IOException {
        int fileNameLength;
        String filename;
        if (clientSocketChannel == null) {
            return;
        }
        if (splitMessage.length != 2) {
            System.out.println("Wrong command");
            return;
        }
        clientByteBuffer = ByteBuffer.allocate(DEFAULT_BYTE_BUFFER_SIZE);
        filename = splitMessage[1];
        fileNameLength = filename.length();
        clientByteBuffer.put((byte) 4);
        clientByteBuffer.putInt(fileNameLength);
        clientByteBuffer.put(filename.getBytes());
        clientByteBuffer.flip();
        while (clientByteBuffer.hasRemaining()) {
            clientSocketChannel.write(clientByteBuffer);
        }
    }

    private static void sendAuth(String[] splitMessage) throws IOException {
        if (clientSocketChannel == null) {
            return;
        }
        if (splitMessage.length != 2) {
            System.out.println("Wrong command");
            return;
        }
        clientByteBuffer = ByteBuffer.allocate(5 + splitMessage[1].length());
        clientByteBuffer.put((byte) 5);
        clientByteBuffer.putInt(splitMessage[1].length());
        clientByteBuffer.put(splitMessage[1].getBytes());
        clientByteBuffer.flip();
        clientSocketChannel.write(clientByteBuffer);
    }

    private static void changeDir(String[] splitMessage) throws IOException {
        if (clientSocketChannel == null) {
            return;
        }
        if (splitMessage.length != 2) {
            System.out.println("Wrong command");
            return;
        }
        clientByteBuffer = ByteBuffer.allocate(DEFAULT_BYTE_BUFFER_SIZE);
        String targetDir = splitMessage[1];
        int targetDitLength = targetDir.length();
        clientByteBuffer.put((byte) 3);
        clientByteBuffer.putInt(targetDitLength);
        clientByteBuffer.put(targetDir.getBytes());
        clientByteBuffer.flip();
        while (clientByteBuffer.hasRemaining()) {
            clientSocketChannel.write(clientByteBuffer);
        }
    }

    public static void receiveBytesFromServer(SelectionKey key) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        clientSocketChannel.read(byteBuffer);
        byteBuffer.flip();
        byte commandType = byteBuffer.get();
        switch (commandType) {
            case 1:
                receiveCurrentDirFromServer(key);
                break;
            case 2:
                receiveFile(key, clientSocketChannel, Paths.get("project/client/src/main/resources"));
                System.out.print("File uploaded successfully\n>");
        }
    }

    private static void receiveCurrentDirFromServer(SelectionKey key) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BYTE_BUFFER_SIZE);
        clientSocketChannel.read(byteBuffer);
        byteBuffer.flip();
        int lengthDir = byteBuffer.getInt();
        byteBuffer.compact();
        clientSocketChannel.read(byteBuffer);
        byteBuffer.flip();
        StringBuilder sb = new StringBuilder();
        while (lengthDir > 0) {
            while (byteBuffer.hasRemaining() && lengthDir > 0) {
                sb.append((char) byteBuffer.get());
                lengthDir--;
            }
        }
        System.out.println(sb.toString());
        System.out.print("> ");
    }


    private static void handleReadFromServer() {
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
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

///////////////////////////////////////
///////////////////////////////////////
///////////////////////////////////////
//    Серверная часть

    public static void serverCommandInterpretation(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();
        byte command = byteBuffer.get();
        byteBuffer.compact();
        switch (command) {
            case 1:
                receiveFile(key, socketChannel, ((UserInfo) key.attachment()).getCurrentPath());
                sendMessageToClient(key, socketChannel,"File uploaded successfully");
                break;
            case 2:
                makeDir(key, socketChannel);
                break;
            case 3:
                changeClientDirOnServer(key, socketChannel);
                break;
            case 4:
                sendToClient(key, socketChannel);
                break;
            case 5:
                authorizationOnServer(key, socketChannel);
        }
    }

    private static void changeClientDirOnServer(SelectionKey key, SocketChannel socketChannel) throws IOException {
        String targetP = getStringFromBytes(socketChannel);
        Path targetPath = Paths.get(targetP);
        Path currDir = ((UserInfo) key.attachment()).getCurrentPath();


        Path newPath = currDir.resolve(targetPath);




//        Path newPath = currDir.relativize(targetPath);
        if(!Files.exists(newPath)) {
            sendMessageToClient(key, socketChannel, "Wrong directory");
            return;
        }
        newPath = newPath.normalize();
        ((UserInfo) key.attachment()).setCurrentPath(newPath);
        sendMessageToClient(key, socketChannel,  "Current directory: " + newPath);
    }

    public static void receiveFile(SelectionKey key, SocketChannel socketChannel, Path path) throws IOException {
        String fileName = getStringFromBytes(socketChannel);
        socketChannel.read(serverByteBuffer);
        serverByteBuffer.flip();
        int sizeFile = serverByteBuffer.getInt();
        ByteBuffer byteBuffer = ByteBuffer.allocate(sizeFile);
//                   Собираем файл из буфера
        while (sizeFile > 0) {
            while (serverByteBuffer.hasRemaining() && sizeFile > 0) {
                byteBuffer.put(serverByteBuffer.get());
                sizeFile--;
            }
            serverByteBuffer.clear();
            socketChannel.read(serverByteBuffer);
            serverByteBuffer.flip();
        }
        Path pathFile = Paths.get(path + File.separator + fileName);
        try {
            Files.write(pathFile, byteBuffer.array());
        } catch (IOException e) {
            sendMessageToClient(key, socketChannel, "File transfer error");
        }
    }

    private static void makeDir(SelectionKey key, SocketChannel socketChannel) throws IOException {
        String dirName = getStringFromBytes(socketChannel);
        Path userCurrentPath = ((UserInfo) key.attachment()).getCurrentPath();
        Path targetPath = Paths.get(dirName);

        targetPath = userCurrentPath.resolve(targetPath);
        if (Files.exists(targetPath)) {
            sendMessageToClient(key, socketChannel, "The directory already exists");
            return;
        }
        try {
            Files.createDirectories(targetPath);
            sendMessageToClient(key, socketChannel, "The directory was created successfully");
        } catch (IOException e) {
            sendMessageToClient(key, socketChannel, "Unable to create a directory");
        }
        System.out.println(Files.exists(targetPath));
    }

    private static void sendToClient(SelectionKey key, SocketChannel socketChannel) throws IOException {
        String fileName = getStringFromBytes(socketChannel);
        Path currentDir = getCurrentDir(key, socketChannel);
        Path targetFilePath = currentDir.resolve(Paths.get(fileName));
        if (!Files.exists(targetFilePath)) {
            return;
        }
        serverByteBuffer.clear();
        serverByteBuffer.put((byte) 2);
        int fileNameLength = fileName.length();
        serverByteBuffer.putInt(fileNameLength);
        serverByteBuffer.put(fileName.getBytes());
        serverByteBuffer.putInt((int) Files.size(targetFilePath));
        FileChannel srcFileChannel = (FileChannel) Files.newByteChannel(targetFilePath);
        while (srcFileChannel.read(serverByteBuffer) != 0) {
            serverByteBuffer.flip();
            socketChannel.write(serverByteBuffer);
            serverByteBuffer.clear();
        }
        srcFileChannel.close();
    }

    private static Path getCurrentDir(SelectionKey key, SocketChannel socketChannel) {
        UserInfo userInfo = (UserInfo) key.attachment();
        return userInfo.getCurrentPath();
    }

    private static void sendMessageToClient(SelectionKey key, SocketChannel socketChannel, String message) throws IOException {
        serverByteBuffer.clear();
        serverByteBuffer.put((byte) 1);
        serverByteBuffer.putInt(message.length());
        serverByteBuffer.put(message.getBytes());
        serverByteBuffer.flip();
        socketChannel.write(serverByteBuffer);
    }

    private static void authorizationOnServer(SelectionKey key, SocketChannel socketChannel) throws IOException {
        String userName = getStringFromBytes(socketChannel);
        ((UserInfo) key.attachment()).setName(userName);
        Path path = ((UserInfo) key.attachment()).getCurrentPath().getParent().resolve(Paths.get(userName));
        ((UserInfo) key.attachment()).setRootPath(path);
        ((UserInfo) key.attachment()).setCurrentPath(path);
        if(!Files.exists(path)) {
            Files.createDirectory(path);
        }
        String message = "You are logged in as " + ((UserInfo) key.attachment()).getName() +
                "\nCurrent directory " + path.getFileName() + File.separator;
        sendMessageToClient(key,  socketChannel, message);
    }

    private static String getStringFromBytes(SocketChannel socketChannel) throws IOException {
        serverByteBuffer.clear();
        socketChannel.read(serverByteBuffer);
        serverByteBuffer.flip();
        int fileNameLength = serverByteBuffer.getInt();
        serverByteBuffer.compact();
        StringBuilder sb = new StringBuilder();
        while (fileNameLength > 0) {
            socketChannel.read(serverByteBuffer);
            serverByteBuffer.flip();
            while (serverByteBuffer.hasRemaining() && fileNameLength > 0) {
                sb.append((char) serverByteBuffer.get());
                fileNameLength--;
            }
            serverByteBuffer.compact();
        }
        String fileName = sb.toString();
        return fileName;
    }
}
