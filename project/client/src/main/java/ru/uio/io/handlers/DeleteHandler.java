package ru.uio.io.handlers;

import ru.uio.io.Common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DeleteHandler extends BaseHandler {
    public DeleteHandler(DataInputStream in, DataOutputStream out) {
        super(in, out);
    }

    @Override
    public void workWithMessage() {
        try {
            out.write(Common.createDataPacket("116".getBytes("UTF8"), command.getBytes("UTF8")));
            out.flush();
            boolean loopBreak = false;
            while (true) {
                byte[] initilize = new byte[1];
                in.read(initilize, 0, initilize.length);
                if (initilize[0] == 2) {
                    System.out.println("read = 2");
                    byte[] cmdBuff = new byte[3];
                    in.read(cmdBuff, 0, cmdBuff.length);
                    byte[] recvData = Common.readStream(in);
                    switch (Integer.parseInt(new String(cmdBuff))) {
                        case 119://if file not found in store
                            System.out.println(new String(recvData));
                            loopBreak = true;
                    }
                }
                if (loopBreak == true) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
