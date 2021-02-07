package ru.daniilazarnov;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

public class Client {
    public static void main(String[] args) throws Exception {
        CountDownLatch networkStarter = new CountDownLatch(1);
        new Thread(() -> Network.getInstance().start(networkStarter)).start();
        networkStarter.await();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String str = in.readLine();
            if ("UF".equals(str)) {
                FileManager.sendFile(Network.getInstance().getCurrentChannel(), future -> {
                    if (!future.isSuccess()) {
                        future.cause().printStackTrace();
//                Network.getInstance().stop();
                    }
                    if (future.isSuccess()) {
                        System.out.println("Файл успешно передан");
//                Network.getInstance().stop();
                    }
                });
            } else if ("DF".equals(str)) {
                // do something
            } else if ("AF".equals(str)) {
                // do something
            } else {
                // do something
            }
        }
    }
}
