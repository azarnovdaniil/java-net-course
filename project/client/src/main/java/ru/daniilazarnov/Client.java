package ru.daniilazarnov;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;


public class Client {
    public static final int PORT = 8189;
    public static final String HOST = "localhost";
    private static final Integer MAGIC_BYTE = 21;

    private String newData;
    private SocketChannel socketClient;
    private ByteBuffer buf = ByteBuffer.allocate(256);
    private ByteBuffer msgBuf = ByteBuffer.allocate(256); ;
    private String clientStorage = "C:\\clientStorage";


    public Client(){}

    public void connect() throws IOException {
        socketClient = SocketChannel.open();
        socketClient.connect(new InetSocketAddress(HOST, PORT));
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
            buf.clear();
        }
    }
    private void writeChannel(String msg) throws IOException {
        msgBuf = ByteBuffer.wrap(msg.getBytes());
        socketClient.write(msgBuf);
        msgBuf.rewind();
    }
    private void writeBuffer(String msg) throws IOException {
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
            while (scanner.hasNext()){
                enterData = scanner.nextLine();
                client.writeChannel(enterData);

                String[] strings = enterData.split(" ");
               if (enterData.startsWith("/upload")){
                   String  s = client.clientStorage+"\\"+strings[1];
                   Integer fileSize = Math.toIntExact(Files.size(Path.of(s)));
                    client.writeChannel(String.valueOf(fileSize));

                   byte[] fileContent = Files.readAllBytes(Path.of(s));
                   client.socketClient.write(ByteBuffer.wrap(fileContent));
               }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            client.disconnectClient();
        }
    }
    private static void writeFile(String newData)
            throws IOException {

        String input = "Content to be written to the file.";
        System.out.println("Input string: " + input);
        byte [] byteArray = input.getBytes();

        ByteBuffer buffer = ByteBuffer.wrap(byteArray);

        AsynchronousFileChannel channel = AsynchronousFileChannel.open(Path.of(newData), StandardOpenOption.WRITE);

        CompletionHandler handler = new CompletionHandler() {

            @Override
            public void completed (Object result, Object attachment) {
            }

            @Override
            public void failed(Throwable e, Object attachment) {

                System.out.println(attachment + " failed with exception:");
                e.printStackTrace();
            }
        };

        channel.write(buffer, 0, "Write operation ALFA", handler);

        channel.close();
    }

}
