package ru.daniilazarnov;

import java.io.*;

public class MainServer {
    public static void main(String[] args) throws Exception {
    new Server(Common.readConfig().getPort()).run();

    }
}
