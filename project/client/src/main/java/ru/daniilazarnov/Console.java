package ru.daniilazarnov;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Scanner;
import java.util.function.Consumer;


public class Console implements Runnable {
    Consumer<String> messageSort;
    private static final Logger LOGGER = LogManager.getLogger(Console.class);
    static final Marker toCon = MarkerManager.getMarker("CONS");

    Console(Consumer<String> messageSort) {
        this.messageSort = messageSort;
    }

    @Override
    public synchronized void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Please enter command. Print help for command list.");
            String str = scanner.nextLine();
            messageSort.accept(str);
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.error(toCon,"SWW in console step-waiting.",LOGGER.throwing(e));
            }
        }
    }


    public synchronized void print(String toPrint) {
        System.out.println(toPrint);
        notify();
    }

}
