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
    static final Marker TO_CON = MarkerManager.getMarker("CONS");

    /**
     * Input console. Waits for inputted command to be done before next iteration.
     *
     * @param messageSort - Consumer to pass the String from the console input to the analyser.
     */

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
                LOGGER.error(TO_CON, "SWW in console step-waiting.", LOGGER.throwing(e));
            }
        }
    }

    /**
     * Prints out the result of command and inits next console input iteration.
     *
     * @param toPrint - command execution result.
     */
    public synchronized void print(String toPrint) {
        System.out.println(toPrint);
        notify();
    }

}
