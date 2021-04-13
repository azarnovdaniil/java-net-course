package ru.daniilazarnov;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        CLI cli = new CLI();
        cli.connect(Common.readConfig().getHost(), Common.readConfig().getPort(), Common.readConfig().getClientRepo());

    }
}
