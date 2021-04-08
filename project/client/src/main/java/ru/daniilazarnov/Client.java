package ru.daniilazarnov;

import java.nio.file.Paths;


public class Client {

    public static void main(String[] args) {

        new ClientModuleManager(Paths.get("ClientConfig.cfg")).start();

    }
}

