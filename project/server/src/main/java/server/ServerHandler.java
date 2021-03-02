package server;

import commands.Delete;
import commands.Rename;
import commands.UpLoad;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import ru.daniilazarnov.FileMessage;
import ru.daniilazarnov.FileRequest;
import commands.DownLoad;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;


public class ServerHandler extends ChannelInboundHandlerAdapter {

    protected static final String WAY_SERVER = ("project/server/src/main/java/file/");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился ");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Клиент отключился");
    }

    @Override
    public void channelRead(io.netty.channel.ChannelHandlerContext ctx, java.lang.Object msg)
            throws java.lang.Exception {


        // Отправка пакета на клиент

        if (msg instanceof FileRequest) {

            UpLoad.upLoad(ctx, msg);
        }

        // Прием пакета от клиента

        if (msg instanceof FileMessage) {
            DownLoad.download(msg);
        }

        //вывод всех файлов

        if (msg instanceof String) {
            String message = (String) msg;
            String[] messageCommand = message.split("\\s");
            String command = messageCommand[0];
            String file = messageCommand[1];
            String account = messageCommand[1];


            if (command.equals("/удалить")) {

                Delete.delete(account, message, file, ctx, msg);

            }
            if (command.equals("/переименовать")) {
                String newNameFile = messageCommand[3];
                String oldNameFile = messageCommand[2];

                Rename.renameFileFromServer(account, ctx, oldNameFile, newNameFile);
            }
            if (command.equals("/создать")) {
                createAccountFromServer(account);
            }
        }


    }

    private void createAccountFromServer(String account) {
        if (!Files.exists(Path.of(WAY_SERVER, account))) {
            new File(WAY_SERVER, account).mkdir();
            System.out.println("Пользователь " + account + " создан");
        } else {
            System.out.println("Вошел " + account);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}
