package ru.daniilazarnov.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;
import ru.daniilazarnov.enumeration.Command;
import ru.daniilazarnov.ReceivingFiles;
import ru.daniilazarnov.auth.AuthClient;
import ru.daniilazarnov.console_IO.OutputConsole;
import ru.daniilazarnov.files_method.FileList;

import java.io.IOException;

public class NetworkHandler {
    private static final Logger LOG = Logger.getLogger(NetworkHandler.class);
//    private AuthClient authentication = new AuthClient();

    void networkHandler(ChannelHandlerContext ctx, Object msg, ByteBuf buf, byte readed, Command command)
            throws IOException {
        switch (command) {

            case DOWNLOAD:
                ReceivingFiles.fileReceive(msg, AuthClient.getUserFolder());
                OutputConsole.printPrompt(); // вывод строки приглашения к вводу
                break;
            case AUTH:
                new Thread(() -> {
                    authentication.authentication(buf, ctx);
                    LOG.debug("Authorization: the response came from the server");
                }).start();
                break;
            case LS:
                System.out.println(FileList.getFilesListStringFromServer(buf));
                OutputConsole.setConsoleBusy(false);
                break;
            default:
                System.err.println("(class ClientHandler) ERROR: Invalid first byte - " + readed);
        }
    }
}
