package ru.uio.io.commands;

import ru.uio.io.Common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class CreateNewFileCommand implements Command {
    private final DataInputStream in;
    private final DataOutputStream out;
    private final String storePath;
    private final String fileName;

    public CreateNewFileCommand(DataInputStream in, DataOutputStream out, String storePath, byte[] recvData) {
        this.in = in;
        this.out = out;
        this.storePath = storePath;
        this.fileName = new String(recvData);
    }

    @Override
    public void execute() {
        try{
            RandomAccessFile rw = null;
            long currentFilePointer = 0;
            boolean loopBreak = false;
            System.out.println(String.format("store/%s/%s", storePath, fileName));
            rw = new RandomAccessFile(String.format("store/%s/%s", storePath, fileName), "rw");
            System.out.println("124");
            out.write(Common.createDataPacket("125".getBytes(StandardCharsets.UTF_8),
                    String.valueOf(currentFilePointer).getBytes(StandardCharsets.UTF_8)));
            out.flush();
            while (true){
                byte[] initilize = new byte[1];
                in.read(initilize, 0, initilize.length);
                if (initilize[0] == 2) {
                    byte[] cmdBuff = new byte[3];
                    in.read(cmdBuff, 0, cmdBuff.length);
                    byte[] recvData = Common.readStream(in);
                    switch (Integer.parseInt(new String(cmdBuff))) {
                        case 126://write data in file (upload)
                            System.out.println("126");
                            rw.seek(currentFilePointer);
                            rw.write(recvData);
                            currentFilePointer = rw.getFilePointer();
                            System.out.println("Download percentage: " + ((float) currentFilePointer / rw.length()) * 100 + "%");
                            out.write(Common.createDataPacket("125".getBytes(StandardCharsets.UTF_8), String.valueOf(currentFilePointer).getBytes(StandardCharsets.UTF_8)));
                            out.flush();
                            break;
                        case 127://close file (upload)
                            if ("Close".equals(new String(recvData))) {
                                loopBreak = true;
                            }
                            break;
                    }
                }
                if (loopBreak) {
                    assert rw != null;
                    rw.close();
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
