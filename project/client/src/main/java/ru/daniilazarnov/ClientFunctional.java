package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.Scanner;

public class ClientFunctional {
    protected void getInfo(){
        System.out.println("Command list: ");
        for (Command cmd : Command.values()){
            System.out.println("\t" + cmd.getCommand() + cmd.getDescription());
        }
    }

    protected void list(DataMsg dataMsg){
        byte[] obj = dataMsg.getBytes();
        String paths = null;
        try {
            paths = (String) ConvertToByte.deserialize(obj);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String[] list = paths.split(";");
        for (String o : list) {
            System.out.println(o);
        }
    }

    protected void downloadFile(DataMsg dataMsg) {
        System.out.print("Enter path to file: ");
        Command download = Command.DOWNLOAD;


    }

    protected void uploadFile(ChannelHandlerContext ctx, Scanner scanner){

    }

    protected void removeFile(ChannelHandlerContext ctx, Scanner scanner){

    }

    protected void moveFile(ChannelHandlerContext ctx, Scanner scanner){

    }

    protected DataMsg createMsg(Command command, Object obj){
        try {
            return new DataMsg(command, ConvertToByte.serialize(obj));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return new DataMsg(Command.createError(""), null);
        }
    }
}
