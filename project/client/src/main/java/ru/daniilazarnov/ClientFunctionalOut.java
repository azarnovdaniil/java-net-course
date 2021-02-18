package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
    Этот класс отвечает за функционал на стороне клиента
 */
public class ClientFunctionalOut {

    protected void getInfo(){
        System.out.println("Command list: ");
        for (Command cmd : Command.values()){
            if (!cmd.getCommand().equals("")) {
                System.out.println("\t" + cmd.getCommand() + cmd.getDescription());
            }
        }
    }

    protected String[] dialog(Scanner scanner, String... arg) {
        List<String> list = new ArrayList<>();
        for (String s : arg) {
            System.out.println(s);
            list.add(scanner.nextLine());
        }
        return list.toArray(String[]::new);
    }


}