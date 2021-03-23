package ru.daniilazarnov;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;


public class Client {
    public static final int PORT = 8189;
    private String newData;
    private SocketChannel socketClient;
    private ByteBuffer buf = ByteBuffer.allocate(256);
    private ByteBuffer msgBuf = ByteBuffer.allocate(256); ;


    public Client(){}

    public void connect() throws IOException {
    socketClient = SocketChannel.open();
    socketClient.connect(new InetSocketAddress("localhost", PORT));
         }
        private void readChannel() throws IOException {
            StringBuilder sb = new StringBuilder();
            buf.clear();
            int read = 0;
            while (socketClient.read(buf) > 0) {
                buf.flip();
                byte[] bytes = new byte[buf.limit()];
                buf.get(bytes);
                System.out.println(new String(bytes));
                buf.compact();

            }
            System.out.println("asdasdasd");
        }
        private void writeChannel(String msg) throws IOException {
            msgBuf = ByteBuffer.wrap(msg.getBytes());
            socketClient.write(msgBuf);
            msgBuf.rewind();
    }
    private void disconnectClient(){
        try {
            socketClient.finishConnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String enterData;
        Client client = new Client();
        try {
            client.connect();
            new Thread(() -> {
                try {
                    client.readChannel();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            while (true){
                enterData = scanner.nextLine();
                client.writeChannel(enterData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            client.disconnectClient();
        }
    }
}
