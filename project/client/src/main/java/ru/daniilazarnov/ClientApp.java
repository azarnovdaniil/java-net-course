package ru.daniilazarnov;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientApp {
    private final ServerHandler client;

    public ClientApp(String host, int port) {
        client = new ServerHandler(host, port);
        write();
        read();
    }

    private void write() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(this::send);
        executorService.shutdown();
    }

    private void send() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("!- Enter command -help for help.");
        while (true) {
            System.out.print(":: ");
            String message = scanner.next();

            if (message.startsWith("-exit")) {
                client.sendMessage(message);
                break;
            } else if (message.startsWith("-help")) {
                System.out.println("The following commands are supported:");
                System.out.println("-exit       Disconnect and exit.");
            } else if (message.startsWith("-")) {
                System.out.printf("!- Command %s is not supported.\n", message);
            }
            else {
                client.sendMessage(message);
            }
        }

    }

    private void read() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(this::receive);
        executorService.shutdown();
    }

    private void receive() {
        // To be implemented.
    }

}

