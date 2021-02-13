package ru.uio.io.handlers;

import ru.uio.io.Common;

import java.io.*;

public class DownloadHandler extends BaseHandler {
    public DownloadHandler(DataInputStream in, DataOutputStream out) {
        super(in, out);
    }

    @Override
    public void workWithMessage() {
        try {
            out.write(Common.createDataPacket("115".getBytes("UTF8"), command.getBytes("UTF8")));
            out.flush();
            long current_file_pointer = 0;
            boolean loop_break = false;
            RandomAccessFile rw = null;
            while (true) {
                byte[] initilize = new byte[1];
                in.read(initilize, 0, initilize.length);
                if (initilize[0] == 2) {
                    System.out.println("read = 2");
                    byte[] cmd_buff = new byte[3];
                    in.read(cmd_buff, 0, cmd_buff.length);
                    byte[] recv_data = Common.readStream(in);
                    switch (Integer.parseInt(new String(cmd_buff))) {
                        case 124://create new file from store (upload)
                            System.out.println(String.format("download/%s", new String(recv_data)));
                            rw = new RandomAccessFile(String.format("download/%s", new String(recv_data)), "rw");
                            out.write(Common.createDataPacket("125".getBytes("UTF8"), String.valueOf(current_file_pointer).getBytes("UTF8")));
                            out.flush();
                            break;
                        case 126:
                            System.out.println("126");
                            rw.seek(current_file_pointer);
                            rw.write(recv_data);
                            current_file_pointer = rw.getFilePointer();
                            System.out.println("Download percentage: " + ((float)current_file_pointer/rw.length())*100+"%");
                            out.write(Common.createDataPacket("125".getBytes("UTF8"), String.valueOf(current_file_pointer).getBytes("UTF8")));
                            out.flush();
                            break;
                        case 127:
                            if ("Close".equals(new String(recv_data))) {
                                loop_break = true;
                            }
                            break;
                        case 119://if file not found in store
                            System.out.println(new String(recv_data));
                            loop_break = true;
                    }
                }
                if (loop_break == true) {
                    if(rw != null)
                        rw.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
