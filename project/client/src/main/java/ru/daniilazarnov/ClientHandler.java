package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;
import java.util.Scanner;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private ClientFunctionalIn cfIn = new ClientFunctionalIn();
    private ClientFunctionalOut cfOut = new ClientFunctionalOut();
    private DataMsg dataMsgRead;
    private DataMsg dataMsgLoad;
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Connection completed");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof DataMsg){
            dataMsgRead = (DataMsg) msg;
        } else {
            System.out.println("Incorrect data from server");
            ctx.writeAndFlush(Command.ERROR);
            return;
        }

        switch (dataMsgRead.getCommand()){
            case LIST:
                cfIn.list(dataMsgRead);
                break;
            case DOWNLOAD:
                cfIn.downloadFile(dataMsgRead, scanner);
                break;
            case UPLOAD:
                break;
            case REMOVE:
                break;
            case EXIT:
                //ctx.channel().close();
                break;
        }
        sendCommand(ctx, scanner);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(Arrays.toString(cause.getStackTrace()));
    }

    private void sendCommand(ChannelHandlerContext ctx, Scanner scanner) {
        //new Thread(() -> {

            //while (true) {
                System.out.print("Enter command (enter /help for to get a list of commands): ");
                switch (scanner.nextLine()) {
                    case "/list":
                        ctx.writeAndFlush(new DataMsg(Command.LIST, null));
                        break;
                    case "/help":
                        cfOut.getInfo();
                        sendCommand(ctx, scanner);
                        break;
                    case "/download":
                        String[] list = cfOut.dialog(scanner,
                                "Enter the path to the downloaded file: ");
                        ctx.writeAndFlush(DataMsg.createMsg(Command.DOWNLOAD, list));
                        //cf.downloadFile(ctx, scanner);
                        break;
                    case "/upload":
                        cfOut.dialog(scanner, "");
                        break;
                    case "/remove":
                        break;
                    case "/move":
                        break;
                    case "/exit":
                        ctx.writeAndFlush(Command.EXIT);
                        break;
                    default:
                        System.out.println("Entered incorrect command, please, try again");
                        sendCommand(ctx, scanner);
                        break;
                }
            //}
       // }).start();

    }


}
