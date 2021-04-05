package ru.kgogolev;

import ru.kgogolev.console.ConsoleHandler;
import ru.kgogolev.network.Client;


public class Application {
    private ConsoleHandler consoleHandler;
    private Client client;

    public Application(Client client) {
        this.client = client;
        this.consoleHandler = new ConsoleHandler(new FileSystem());
        run();
    }

    private void run() {
        while (true) {
            client.sendMessage(consoleHandler.handleMessage());
        }
    }

}

