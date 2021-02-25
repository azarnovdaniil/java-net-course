package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;
import java.util.Scanner;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private final ClientFunctionalIn cfIn = new ClientFunctionalIn();
    private final ClientFunctionalOut cfOut = new ClientFunctionalOut();
    private DataMsg dataMsgRead;
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Connection completed");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof DataMsg) {
            dataMsgRead = (DataMsg) msg;
        } else {
            System.out.println("Incorrect data from server");
            ctx.writeAndFlush(Command.ERROR);
            return;
        }

        switch (dataMsgRead.getCommand()) {
            case LIST:
                cfIn.list(dataMsgRead);
                break;
            case DOWNLOAD:
                cfIn.download(dataMsgRead, scanner);
                break;
            case UPLOAD:
                break;
            case CHECK_FILE_EXIST:
                cfIn.checkFileExistOnServer(ctx, msg);
            case REMOVE:
                cfIn.remove(ctx, dataMsgRead);
                break;
            case EXIT:
                ctx.channel().close();
                break;
            case START:
                cfIn.startClient(dataMsgRead);
                break;
            case NEW_NAME:

        }
        sendCommand(ctx, scanner);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(Arrays.toString(cause.getStackTrace()));
    }

    private void sendCommand(ChannelHandlerContext ctx, Scanner scanner) {

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
                String[] filePath = cfOut.dialog(scanner,
                        "Enter the path to file on client: ");
                cfOut.checkExistOnServer(ctx, filePath);
                break;
            case "/remove":
                String[] fileRemove = cfOut.dialog(scanner,
                        "Enter the path to file on client: ");
                cfOut.remove(ctx, fileRemove);
                break;
            case "/move":
                break;
            case "/exit":
                ctx.writeAndFlush(Command.EXIT);
                break;
            case "/rename":
                String[] fileRename = cfOut.dialog(scanner,
                        "Enter path to file on server which needs to be renamed: ",
                        "Enter new file name: ");
                ctx.writeAndFlush(DataMsg.createMsg(Command.NEW_NAME, fileRename));
            default:
                System.out.println("Entered incorrect command, please, try again");
                sendCommand(ctx, scanner);
                break;
        }
    }
}
