package ru.daniilazarnov;

import java.util.Scanner;
import java.util.function.Consumer;


public class Console implements Runnable {
    Consumer<String> messageSort;

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
                throw new RuntimeException("SWW in console step-waiting.");
            }
        }
    }


    public synchronized void print(String toPrint) {
        System.out.println(toPrint);
        notify();
    }

}
