package ru.daniilazarnov;

import java.nio.file.Paths;



public class Client {

    public static void main(String[] args){

        new ClientModuleManager(
                Paths.get("client", "src", "main", "resources", "config.cfg")
        ).start();

    }
}

