package ru.daniilazarnov;

import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;


public class Client {

    public static void main(String[] args) throws Exception {
        CountDownLatch networkStarter = new CountDownLatch(1);
        new Thread(() -> Network.getInstance().start(networkStarter)).start();
        networkStarter.await();

        FilesSender.sendFile(Paths.get("C:\\Users\\Stas\\Desktop\\Java.NET\\java-net-course\\project\\client\\src\\main\\java\\ru\\daniilazarnov\\demo.txt"), Network.getInstance().getCurrentChannel(), future -> {
            if (!future.isSuccess()) {
                future.cause().printStackTrace();
//                Network.getInstance().stop();
            }
            if (future.isSuccess()) {
                System.out.println("Файл успешно передан");
//                Network.getInstance().stop();
            }
        });
    }
}