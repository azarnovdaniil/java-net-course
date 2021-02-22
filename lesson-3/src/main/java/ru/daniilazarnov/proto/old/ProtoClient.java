package ru.daniilazarnov.proto.old;

import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

public class ProtoClient {
    public static void main(String[] args) throws Exception {
        CountDownLatch networkStarter = new CountDownLatch(1);
        new Thread(() -> Network.getInstance().start(networkStarter)).start();
        networkStarter.await();

        ProtoFileSender.sendFile(Paths.get("D:\\testDir\\DanMachiS2.avi"), Network.getInstance().getCurrentChannel(), future -> {
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
