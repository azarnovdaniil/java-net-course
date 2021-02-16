package ru;


public class StorageClient {

    private final static String IP_ADDR = "localhost";

    private final static int PORT = 60111;


    public void run()  {

        new NettyClient(this, IP_ADDR, PORT).run();
    }




}


