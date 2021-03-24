package ru.daniilazarnov;

import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        int port = 7777;
        String host = "127.0.0.1";
       RepoServer server = new RepoServer(host, port);
        System.out.println(CommandList.upload.ordinal());
       server.start();


    }
}
