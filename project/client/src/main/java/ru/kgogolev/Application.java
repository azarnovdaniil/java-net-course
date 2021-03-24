package ru.kgogolev;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Application {
    private ConsoleHandler consoleHandler;
    private Client client;

    public Application(Client client) {
        this.client = client;
        this.consoleHandler = new ConsoleHandler(new FileSystem());
        run();
    }

    private void run(){
        while (true){
            client.sendMessage(consoleHandler.handleMessage());
        }
    }
//    private void consoleMessageHandler(){
//        String message=null;
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
//            while (true) {
//
//                message = br.readLine();
//                if (message.startsWith("111")){
//                    client.sendMessage(message);
//                }else {
//                    System.out.println(message);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



    }

