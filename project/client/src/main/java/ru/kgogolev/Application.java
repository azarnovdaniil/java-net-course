package ru.kgogolev;

import io.netty.buffer.Unpooled;
import ru.kgogolev.console.ConsoleHandler;
import ru.kgogolev.network.Client;

import java.nio.charset.StandardCharsets;


public class Application {
    private ConsoleHandler consoleHandler;
    private Client client;

    public Application(Client client) {
        this.client = client;
        this.consoleHandler = new ConsoleHandler(new FileSystem());
        run();
    }

    private void run() {
//        client.sendMessage(Unpooled.wrappedBuffer("/auth 111111".getBytes(StandardCharsets.UTF_8)));
//        client.sendMessage(Unpooled.wrappedBuffer("/auth Vasya".getBytes(StandardCharsets.UTF_8)));
        while (true) {
            client.sendMessage(consoleHandler.handleMessage());
        }
    }

}

