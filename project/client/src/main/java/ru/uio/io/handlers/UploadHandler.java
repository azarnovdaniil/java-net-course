package ru.uio.io.handlers;

import ru.uio.io.Common;

import javax.swing.*;
import java.io.*;

public class UploadHandler extends BaseHandler {
    private JFileChooser jfc = new JFileChooser();
    public UploadHandler(DataInputStream in, DataOutputStream out) {
        super(in, out);
    }

    @Override
    public void workWithMessage() {
        int dialog_value = jfc.showOpenDialog(null);
        if (dialog_value == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            try {
                out.write(Common.createDataPacket("124".getBytes("UTF8"), file.getName().getBytes("UTF8")));
                out.flush();
                RandomAccessFile rw = new RandomAccessFile(file, "r");
                long current_file_pointer = 0;
                boolean loop_break = false;
                while (true) {
                    if(in.read() == 2){
                        byte[] cmd_buff = new byte[3];
                        in.read(cmd_buff, 0, cmd_buff.length);
                        byte[] recv_buff = Common.readStream(in);
                        switch (Integer.parseInt(new String(cmd_buff))) {
                            case 125:
                                current_file_pointer = Long.valueOf(new String(recv_buff));
                                int buff_len = (int) (rw.length() - current_file_pointer < 20000 ? rw.length() - current_file_pointer : 20000);
                                System.out.println("buff_len " + buff_len);
                                byte[] temp_buff = new byte[buff_len];
                                if (current_file_pointer != rw.length()) {
                                    rw.seek(current_file_pointer);
                                    rw.read(temp_buff, 0, temp_buff.length);
                                    out.write(Common.createDataPacket("126".getBytes("UTF8"), temp_buff));
                                    out.flush();
                                    System.out.println("Upload percentage: " + ((float)current_file_pointer/rw.length())*100+"%");
                                } else {
                                    loop_break = true;
                                    out.write(Common.createDataPacket("127".getBytes("UTF8"), "Close".getBytes("UTF8")));
                                    out.flush();
                                }
                                break;
                        }
                    }
                    if (loop_break == true) {
                        rw.close();
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("SWW7", e);
            }
        }
    }
}
