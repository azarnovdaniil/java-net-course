package ru.daniilazarnov.server.operations;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import ru.daniilazarnov.common.commands.Commands;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.common.messages.TextMessageSender;
import ru.daniilazarnov.server.auth.AuthService;
import ru.daniilazarnov.server.operations.download.DownloadHandler;
import ru.daniilazarnov.server.operations.exit.ExitHandler;
import ru.daniilazarnov.server.operations.login.LoginHandler;
import ru.daniilazarnov.server.operations.show.ShowHandler;
import ru.daniilazarnov.server.operations.upload.UploadHandler;

public class ServerOperationsFactory {

    private final AuthService authService;

    public ServerOperationsFactory(AuthService authService) {
        this.authService = authService;
    }

    public Handler createOperation(byte code, Channel channel, String root, ByteBuf buf) {
        String sessionId = channel.id().toString();
        boolean isAuth = authService.checkSession(sessionId);
        if (!isAuth) {
            if (code == Commands.LOGIN.getCode()) {
                System.out.println("Login operation request is received");
                return new LoginHandler(authService, buf, channel);
            }
            System.out.println("Error: User is unauthorized");
            buf.clear();
            channel.flush();
            new TextMessageSender(channel).sendMessage("Error: User is unauthorized");


            return null;
        }

        String userRoot = root + authService.getUserBySessionId(sessionId) + "/";

        if (code == Commands.DOWNLOAD.getCode()) {
            System.out.println("DOWNLOAD operation request is received");
            return new DownloadHandler(channel, userRoot, buf);
        }
        if (code == Commands.UPLOAD.getCode()) {
            System.out.println("UPLOAD operation request is received");
            return new UploadHandler(userRoot, buf);
        }
        if (code == Commands.SHOW.getCode()) {
            System.out.println("SHOW operation request is received");
            return new ShowHandler(channel, userRoot);
        }
        if (code == Commands.EXIT.getCode()) {
            return new ExitHandler(channel);
        }

        System.out.println("ERROR: Invalid first byte: " + code);
        return null;
    }
}
