package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.Scanner;

public class ClientFunctionalIn {

    protected void list(DataMsg dataMsg){
        byte[] obj = dataMsg.getBytes();
        String paths = null;
        paths = (String) ConvertToByte.deserialize(obj);
        String[] list = paths.split(";");
        for (String o : list) {
            System.out.println(o);
        }
    }

    protected void downloadFile(DataMsg dataMsg, Scanner scanner) {
        System.out.print("Enter path to file: ");

    }

    protected void uploadFile(ChannelHandlerContext ctx, Scanner scanner){

    }

    protected void removeFile(ChannelHandlerContext ctx, Scanner scanner){

    }

    protected void moveFile(ChannelHandlerContext ctx, Scanner scanner){

    }

}
