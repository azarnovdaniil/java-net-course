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
    private static final String FILE_PATH_TEMPLATE = CLIENT_STORAGE + File.separator + "%s";

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
                Command cmd = Command.byCmd(msg);
                ctx.writeAndFlush(cmd.getCmd());
                switch (cmd) {
                    case TEST:
                        System.out.println("Enter data to send");
                        String data = scanner.nextLine();
                        ctx.writeAndFlush(data);
                        break;
                    case UPLOAD:
                        try {

                            String filename;
                            do {
                                System.out.println("Enter filename");
                                filename = scanner.nextLine();
                                if (!FileUtils.isFileExist(String.format(FILE_PATH_TEMPLATE, filename))) {
                                    System.out.println("File not exist. Enter valid filename.");
                                }
                            } while (!FileUtils.isFileExist(String.format(FILE_PATH_TEMPLATE, filename)));

                            ctx.writeAndFlush(filename);
                            System.out.println("Sending file content");
                            String fileContent = Files.readString(Path.of(String.format(FILE_PATH_TEMPLATE, filename)),
                                    StandardCharsets.US_ASCII);
                            ctx.writeAndFlush(fileContent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case EXIT:
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
        LOGGER.info("Message received = " + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("Error " + cause.getMessage(), cause);
        ctx.close();
    }
}
