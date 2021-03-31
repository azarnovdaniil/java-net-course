package ru.daniilazarnov.commands;

import ru.daniilazarnov.ClientConnection;
import ru.daniilazarnov.Commands;
import ru.daniilazarnov.Protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadFileCommand implements ICommand {
    private String[] args;
    private Commands command = Commands.stor;
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public UploadFileCommand(ArgumentsForCommand arguments) {
        this.args = arguments.getArgs();
    }

    @Override
    public boolean apply(ClientConnection connection) throws IOException {
        SocketChannel socketChannel = connection.getClientSocketChannel();
        int fileNameLength;
        String filename;
        if (socketChannel == null) {
            return false;
        }
        if (args.length != 1) {
            System.out.println("Wrong command");
            return false;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        Path pathSrcFile = Paths.get(args[0]);
        Protocol.sendFileToSocketChannel(pathSrcFile, socketChannel);
//        if (!Files.exists(pathSrcFile)) {
//            return false;
//        }
//        byteBuffer.put(command.getNumberOfCommand());
//        filename = pathSrcFile.getFileName().toString();
//        fileNameLength = filename.length();
//        byteBuffer.putInt(fileNameLength);
//        byteBuffer.put(filename.getBytes());
//        byteBuffer.putInt((int) Files.size(pathSrcFile));
//        FileChannel srcFileChannel = (FileChannel) Files.newByteChannel(pathSrcFile);
//        while (srcFileChannel.read(byteBuffer) != 0) {
//            byteBuffer.flip();
//            socketChannel.write(byteBuffer);
//            byteBuffer.clear();
//        }
//        srcFileChannel.close();
        return true;
    }
}
