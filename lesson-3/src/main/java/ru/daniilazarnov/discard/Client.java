package ru.daniilazarnov.discard;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;


public class Client {
    public String HOST;
    public int PORT;
    Channel CHANNEL;

    public Client(String host, int port) {
        this.HOST = host;
        this.PORT = port;
    }

    public void run() throws InterruptedException, IOException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline().addLast(
                                    new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()),
                                    new StringDecoder(),
                                    new StringEncoder(),
                                    new ClientHandler());
                        }
                    });

            CHANNEL = bootstrap.connect(HOST, PORT).sync().channel();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String str = in.readLine();

                if (!"file".equals(str)) {
                    CHANNEL.writeAndFlush(str + "\n");
                } else {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new File("C:\\java_log\\out_file"));
                    int send = chooser.showDialog(null, "Send");
                    if (send == JFileChooser.APPROVE_OPTION) {
                        sendFile(this.CHANNEL, chooser.getSelectedFile().getPath());
                        CHANNEL.writeAndFlush(chooser.getSelectedFile().getPath() + "\n");
                    }
                }
            }
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        new Client("localhost", 8189).run();
    }

    private void sendFile(Channel channel, String path) {
        try {
            //Чтение файла
            File source = new File(path);
            FileInputStream fileIS = new FileInputStream(source);
            InputStreamReader inputSR = new InputStreamReader(fileIS, "windows-1251");

            String namestr = source.getName();
            String[] newfilename = namestr.split(".txt");

            //Запись в файл
            File dest = new File(source.getParent() + "\\" + newfilename[0] + " test.txt");
            FileOutputStream fileOS = new FileOutputStream(dest);
            OutputStreamWriter outSW = new OutputStreamWriter(fileOS, "windows-1251");

            System.out.println(source.length() + " байт");
            int c;
            List<Integer> out = new ArrayList();

            //Передача файла
            while ((c = inputSR.read()) != -1) {
                int integer= c;
                outSW.append((char) integer);
                if (c != 10) {
                    out.add(c);
                } else {
                    out.add(c);
                    channel.writeAndFlush(out + "\n");
                    out.clear();
                }
            }

            out.clear();

            inputSR.close();
            outSW.close();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}