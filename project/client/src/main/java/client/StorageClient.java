package client;


public class StorageClient {

    private static final String IP_ADDR = "localhost";

    private final static int PORT = 60111;

    public void run() {
        new NettyClient(this, IP_ADDR, PORT).run();
    }


}
