package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;
import ru.daniilazarnov.model.RequestData;
import ru.daniilazarnov.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class);

    private static final String CLIENT_STORAGE = "project" + File.separator
            + "client" + File.separator + "storage";
    private static final char SECRET_SEPARATOR = '@';

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        FileUtils.createDir(CLIENT_STORAGE);

        ctx.fireChannelRegistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LOGGER.info("Channel Active!!!");
        Thread t1 = new Thread(() -> {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();
                String[] splitMsg = msg.split(" ", 2);
                String cmd = splitMsg[0];
                String data = splitMsg[1];
                switch (cmd) {
                    case "-test":
                        LOGGER.info("Sending text " + data);
                        ctx.writeAndFlush(cmd + data);
                        break;
                    case "-upload":
                        LOGGER.info("Uploading file " + data);
                        try {
                            ctx.writeAndFlush("upload");
                            String fileContent = Files.readString(Path.of(CLIENT_STORAGE + File.separator + data),
                                    StandardCharsets.US_ASCII);
                            RequestData requestData = new RequestData();
                            requestData.setCommand((byte) 1);
                            char separator = SECRET_SEPARATOR;
                            requestData.setContent(data + separator + fileContent);
                            requestData.setSeparator(separator);
                            requestData.setLength(data.length() + fileContent.length() + 1);
                            LOGGER.info("Sending: " + requestData);
                            ctx.writeAndFlush(msg);
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    case "-exit":
                        LOGGER.info("exit");
                        ctx.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Unknown command");
                }
            }
        });
        t1.start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            LOGGER.info("Message received = " + msg);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("[Client] : Error " + cause.getMessage(), cause);
        ctx.close();
    }
}
