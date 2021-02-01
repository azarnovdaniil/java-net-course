package ru.uio.io;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientApp {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8888);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            AtomicBoolean isClose = new AtomicBoolean();
            File file = new File("file\\image.png");
            isClose.set(false);

            new Thread(() -> {
                try {
                    System.out.println(file.exists());
                    RandomAccessFile rw = new RandomAccessFile(file, "r");
                    long current_file_pointer = 0;
                    boolean loop_break = false;
                    while (true) {
//                        String message = in.readUTF();
//                        if ("-exit".equals(message)){
//                            isClose.set(true);
//                            System.out.println("Time out for login. Please press enter to close command line...");
//                            break;
//                        }

                        if(in.read() == 2){
                            System.out.println("read == 2");
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
                                    }
                                    break;
                            }
                        }
                        if (loop_break == true) {
                            rw.close();
                            isClose.set(true);
                            System.out.println("Time out for login. Please press enter to close command line...");
                            System.out.println("Stop Server informed");
                            out.write(Common.createDataPacket("127".getBytes("UTF8"), "Close".getBytes("UTF8")));
                            out.flush();
                            break;
//                            System.out.println("Stop Server informed");
//                            out.write(Common.createDataPacket("127".getBytes("UTF8"), "Close".getBytes("UTF8")));
//                            out.flush();
//                            socket.close();
//                            System.out.println("Client Socket Closed");
//                            break;
                        }
                        //System.out.println(message);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("SWW7", e);
                }
            }).start();

            Scanner scanner = new Scanner(System.in);
            //Path filePath = null;
            while (true) {
                try {
                    if (isClose.get()){
                        System.out.println("Connect closed");
                        scanner.close();
                        break;
                    }

                    System.out.println("...");
                    String command = scanner.nextLine();

                    if (command.startsWith("-auth")){
                        out.writeUTF(command);
                        continue;
                    }
                    out.write(Common.createDataPacket("124".getBytes("UTF8"), file.getName().getBytes("UTF8")));
                    out.flush();

                } catch (IOException e) {
                    throw new RuntimeException("SWW6", e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("SWW10", e);
        }
    }
}
