package ru.daniilazarnov;

public class Common {

    public static void main(String[] args) {
        new Thread(() -> {
            ServerApp server = new ServerApp();
        }).start();
//        new Thread(() -> {
//            ClientApp client1 = new ClientApp().main([]"0");
//        }).start();
//        new Thread(() -> {
//            ClientApp client2 = new ClientApp().main([]"0");
//        }).start();
//        new Thread(() -> {
//            ClientApp client3 = new ClientApp().main([]"0");
//        }).start();
        System.out.println("Start ServerApp and ClientApp");
        System.out.println("Common!");
    }
}
