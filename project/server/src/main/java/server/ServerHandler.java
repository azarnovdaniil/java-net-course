package server;

import delete.Delete;
import ru.daniilazarnov.MyMessage;
import upload.UpLoad;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import ru.daniilazarnov.FileMessage;
import ru.daniilazarnov.FileRequest;
import download.DownLoad;


import java.io.File;


public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final String WAY_SERVER = ("project/server/src/main/java/file/");

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


            if (command.equals("/удалить")) {

                Delete.delete(message, file, ctx, msg);

            }
            if (command.equals("/переименовать")) {
                String newNameFile = messageCommand[3];
                String oldNameFile = messageCommand[2];


                renameFileFromServer(ctx, oldNameFile, newNameFile);


            }
        }


    }

    private void renameFileFromServer(ChannelHandlerContext ctx, String oldNameFile, String newNameFile) {
        File file = new File(WAY_SERVER, oldNameFile);
        File newFile = new File(WAY_SERVER, newNameFile);

        if (file.renameTo(newFile)) {
            ctx.writeAndFlush(new MyMessage("Файл " + file + " успешно переименован в " + newFile));
        } else {
            ctx.writeAndFlush(new MyMessage("Файл " + file + " НЕ переименован в " + newFile));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)  {
        cause.printStackTrace();
        ctx.close();
    }



}
