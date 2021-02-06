package ru.uio.io;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientApp {
    public static void main(String[] args) {
        try {
            JFileChooser jfc = new JFileChooser();
            Socket socket = new Socket("localhost", 8888);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            AtomicBoolean isClose = new AtomicBoolean();
            isClose.set(false);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                try {
                    if (isClose.get()){
                        System.out.println("Connect closed");
                        scanner.close();
                        break;
                    }

                    System.out.println("Enter command:");
                    String command = scanner.nextLine();

                    if (command.startsWith("-auth")){
                        out.writeUTF(command);
                        out.flush();
                        while (true) {
                            boolean loop_break = false;
                            if(in.read() == 2){
                                byte[] cmd_buff = new byte[3];
                                in.read(cmd_buff, 0, cmd_buff.length);
                                byte[] recv_buff = Common.readStream(in);
                                switch (Integer.parseInt(new String(cmd_buff))) {
                                    case 122:
                                        System.out.println(new String(recv_buff));
                                        loop_break = true;
                                        break;
                                }
                            }
                            if (loop_break == true) {
                                break;
                            }
                        }
                        continue;
                    }
                    if (command.startsWith("-u") || command.startsWith("-upload")){
                        System.out.println("upload");
                        int dialog_value = jfc.showOpenDialog(null);
                        if (dialog_value == JFileChooser.APPROVE_OPTION) {
                            File file = jfc.getSelectedFile();
                            out.write(Common.createDataPacket("124".getBytes("UTF8"), file.getName().getBytes("UTF8")));
                            out.flush();
                            try {
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
                                        //isClose.set(true);
                                        //System.out.println("Time out for login. Please press enter to close command line...");
                                        //System.out.println("Stop Server informed");
                                        //out.write(Common.createDataPacket("127".getBytes("UTF8"), "Close".getBytes("UTF8")));
                                        //out.flush();
                                        break;
                                    }
                                }
                            } catch (IOException e) {
                                throw new RuntimeException("SWW7", e);
                            }
                        }
                        continue;
                    }
                    if (command.startsWith("-l") || command.startsWith("-list")){
                        out.write(Common.createDataPacket("121".getBytes("UTF8"), "list".getBytes("UTF8")));
                        out.flush();
                        while (true) {
                            boolean loop_break = false;
                            if(in.read() == 2){
                                byte[] cmd_buff = new byte[3];
                                in.read(cmd_buff, 0, cmd_buff.length);
                                byte[] recv_buff = Common.readStream(in);
                                switch (Integer.parseInt(new String(cmd_buff))) {
                                    case 121:
                                        System.out.println(new String(recv_buff));
                                        break;
                                    case 120:
                                        if("close".equals(new String(recv_buff))){
                                            loop_break = true;
                                        }
                                        break;
                                }
                            }
                            if (loop_break == true) {
                                break;
                            }
                        }
                        continue;
                    }
                    if (command.startsWith("-d") || command.startsWith("-download")){
                        String[] str = command.split(" ");
                        if(str.length < 1){
                            System.out.println("Please write number file. Like: -d 1");
                            continue;
                        }
                        out.write(Common.createDataPacket("115".getBytes("UTF8"), str[1].getBytes("UTF8")));
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

                    }
                    if (command.startsWith("-exit")){
                        out.write(Common.createDataPacket("120".getBytes("UTF8"), "exit".getBytes("UTF8")));
                        out.flush();
                        isClose.set(true);
                    }


                } catch (IOException e) {
                    throw new RuntimeException("SWW6", e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("SWW10", e);
        }
    }
}
