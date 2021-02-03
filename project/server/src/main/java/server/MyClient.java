package server;

import clientserver.Commands;
import clientserver.commands.CommandLS;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.CharsetUtil;

import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MyClient {
    public static void main(String[] args) {
//        try {
//            byte[] by = new byte[5]; "Синхро".getBytes(StandardCharsets.UTF_8);
////            System.out.println(by);
////            String str = by.toString(StandardCharsets.UTF_8);
////            System.out.println(str);
//            Commands command = Commands.LS;
//
//            by[0] = command.getSignal();
////            by[2] = 112;
////            by[3] = 107;
//            Socket socket = new Socket("localhost", 8189);
//            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//            Scanner in = new Scanner(socket.getInputStream());
//            out.write(by);
////            out.write(new byte[]{112, 110, 101});
//            String x = in.nextLine();
//            System.out.println("A: " + x);
//            in.close();
//            out.close();
//            socket.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        ByteBuffer bb = ByteBuffer.allocate(4);
//        bb.putInt(1256);
//
//        System.out.println(bb.get(0));
//        System.out.println(bb.get(1));
//        System.out.println(bb.get(2));
//        System.out.println(bb.get(3));
        byte[] answer = new byte[0];
        ByteBuf buf;
        answer = CommandLS.makeResponse(List.of("file1.txt","file2.txt"));

        buf = ByteBufAllocator.DEFAULT.directBuffer(answer.length);
        buf.writeBytes(answer);
        buf.readByte();
        List<String> ans = CommandLS.readResponse(buf);
        System.out.println(ans);

    }
}
