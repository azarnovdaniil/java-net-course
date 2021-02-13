package ru.uio.io.commands;

import ru.uio.io.ClientHandler;
import ru.uio.io.Common;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CreateNewFileCommand extends Command {
    private long current_file_pointer;
    private RandomAccessFile rw;
    private byte[] recv_data;

    public CreateNewFileCommand(ClientHandler clientHandler, RandomAccessFile rw,
                                byte[] recv_data, long current_file_pointer) {
        super(clientHandler);
        this.rw = rw;
        this.recv_data = recv_data;
        this.current_file_pointer = current_file_pointer;
    }

    @Override
    public boolean execute() {
        try {
            System.out.println(String.format("store/%s/%s", clientHandler.getStorePath(), new String(recv_data)));
            rw = new RandomAccessFile(String.format("store/%s/%s", clientHandler.getStorePath(), new String(recv_data)), "rw");
            System.out.println("124");
            System.out.println(Files.exists(Paths.get(String.format("store/%s/%s", clientHandler.getStorePath(), new String(recv_data)))));
            clientHandler.getOut().write(Common.createDataPacket("125".getBytes("UTF8"), String.valueOf(current_file_pointer).getBytes("UTF8")));
            clientHandler.getOut().flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
