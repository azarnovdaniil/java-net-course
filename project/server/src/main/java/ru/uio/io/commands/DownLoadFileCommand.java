package ru.uio.io.commands;

import ru.uio.io.Common;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DownLoadFileCommand implements Command{
    private final DataInputStream in;
    private final DataOutputStream out;
    private final String storePath;
    private final Integer fileId;

    public DownLoadFileCommand(DataInputStream in, DataOutputStream out, String storePath, byte[] recvData) {
        this.in = in;
        this.out = out;
        this.storePath = storePath;
        this.fileId = Integer.valueOf(new String(recvData));
    }

    @Override
    public void execute() {
        try {
            List<String> list = Common.getFileList(String.format("store/%s/", storePath));
            if (fileId > list.size()) {
                out.write(Common.createDataPacket("119".getBytes(StandardCharsets.UTF_8), "File not found".getBytes(StandardCharsets.UTF_8)));
                out.flush();
                return;
            }
            long currentFilePointer = 0;
            boolean loopBreak = false;
            System.out.println(String.format("store/%s/%s", storePath, list.get(fileId - 1)));
            RandomAccessFile rw = new RandomAccessFile(String.format("store/%s/%s", storePath, list.get(fileId - 1)), "rw");
            out.write(Common.createDataPacket("124".getBytes(StandardCharsets.UTF_8),
                    list.get(fileId - 1).getBytes(StandardCharsets.UTF_8)));
            out.flush();
            while (true){
                byte[] initilize = new byte[1];
                in.read(initilize, 0, initilize.length);
                if (initilize[0] == 2) {
                    byte[] cmdBuff = new byte[3];
                    in.read(cmdBuff, 0, cmdBuff.length);
                    byte[] recvData = Common.readStream(in);
                    switch (Integer.parseInt(new String(cmdBuff))) {
                        case 125://send file data to client (download)
                            currentFilePointer = Long.parseLong(new String(recvData));
                            int buff_len = (int) (rw.length() - currentFilePointer < 20000 ? rw.length() - currentFilePointer : 20000);
                            System.out.println("buff_len " + buff_len);
                            byte[] temp_buff = new byte[buff_len];
                            if (currentFilePointer != rw.length()) {
                                rw.seek(currentFilePointer);
                                rw.read(temp_buff, 0, temp_buff.length);
                                out.write(Common.createDataPacket("126".getBytes(StandardCharsets.UTF_8), temp_buff));
                                out.flush();
                                System.out.println("Upload percentage: " + ((float) currentFilePointer / rw.length()) * 100 + "%");
                            } else {
                                loopBreak = true;
                            }
                            break;
                    }
                }
                if (loopBreak) {
                    rw.close();
                    out.write(Common.createDataPacket("127".getBytes(StandardCharsets.UTF_8), "Close".getBytes(StandardCharsets.UTF_8)));
                    out.flush();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
