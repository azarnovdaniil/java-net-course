package ru.daniilazarnov;

import java.util.concurrent.CountDownLatch;


public class Client {
    public static void main(String[] args) throws Exception {
        CountDownLatch networkStarter = new CountDownLatch(1);
        new Thread(() -> Network.getInstance().start(networkStarter)).start();
        networkStarter.await();

    }
}
