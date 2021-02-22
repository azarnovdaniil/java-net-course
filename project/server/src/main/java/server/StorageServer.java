package server;


public class StorageServer {


    private final int port = 60111;

    public void run() {
        new NettyServer(port).run();
    }
}
