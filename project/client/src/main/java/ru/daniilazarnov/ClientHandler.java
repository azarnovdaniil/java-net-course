package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private ClientFunctional cf = new ClientFunctional();
    private DataMsg dataMsgRead;
    private DataMsg dataMsgLoad;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sendCommand(ctx);
        //ctx.writeAndFlush(Command.LIST);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        int i = 0;
        if (msg instanceof DataMsg){
            dataMsgRead = (DataMsg) msg;
        } else {
            System.out.println("Incorrect data from server");
            ctx.writeAndFlush(Command.ERROR);
            return;
        }

        switch (dataMsgRead.getCommand()){
            case LIST:
                cf.list(dataMsgRead);
                break;
            case DOWNLOAD:
                cf.downloadFile(dataMsgRead);
                break;
            case UPLOAD:
                break;
            case REMOVE:
                break;
            case EXIT:
                //ctx.channel().close();
                break;
        }

        if (msg instanceof String){
            System.out.println((String) msg);
        }
        if (msg instanceof List) {
            List<Path> list = (List<Path>) msg;
            for (Path o : list) {
                System.out.println(o);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(Arrays.toString(cause.getStackTrace()));
    }

    private void sendCommand(ChannelHandlerContext ctx) {
        //new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Enter command (enter /help for to get a list of commands): ");
                switch (scanner.nextLine()) {
                    case "/list":
                        ctx.writeAndFlush(Command.LIST);
                        break;
                    case "/help":
                        cf.getInfo();
                        break;
                    case "/download":
                        dataMsgLoad = cf.createMsg(Command.DOWNLOAD, "");
                        ctx.writeAndFlush(Command.DOWNLOAD);
                        //cf.downloadFile(ctx, scanner);
                        break;
                    case "/upload":
                        cf.uploadFile(ctx, scanner);
                        break;
                    case "/remove":
                        cf.removeFile(ctx, scanner);
                        break;
                    case "/move":
                        cf.moveFile(ctx, scanner);
                        break;
                    case "/exit":
                        ctx.writeAndFlush(Command.EXIT);
                        break;
                    default:
                        System.out.println("Entered incorrect command, please, try again");
                        break;
                }
            }
       // }).start();

    }


}
