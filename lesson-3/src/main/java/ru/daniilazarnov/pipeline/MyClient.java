package ru.daniilazarnov.pipeline;

import io.netty.util.CharsetUtil;

import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MyClient {
    public static void main(String[] args) {
        try {
            byte[] by = "Синхро".getBytes(StandardCharsets.UTF_8);
//            System.out.println(by);
//            String str = by.toString(StandardCharsets.UTF_8);
//            System.out.println(str);

            Socket socket = new Socket("localhost", 8189);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            Scanner in = new Scanner(socket.getInputStream());
            out.write(by);
//            out.write(new byte[]{112, 110, 101});
            String x = in.nextLine();
            System.out.println("A: " + x);
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
