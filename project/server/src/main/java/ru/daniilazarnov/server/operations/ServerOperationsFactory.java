package ru.daniilazarnov.server.operations;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import ru.daniilazarnov.common.OperationTypes;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.server.auth.AuthService;
import ru.daniilazarnov.server.operations.download.DownloadHandler;
import ru.daniilazarnov.server.operations.login.LoginHandler;
import ru.daniilazarnov.server.operations.show.ShowHandler;
import ru.daniilazarnov.server.operations.upload.UploadHandler;

public class ServerOperationsFactory {

    private final AuthService authService;

    public ServerOperationsFactory(AuthService authService) {
        this.authService = authService;
    }

    public Handler createOperation(byte code, Channel channel, String root, ByteBuf buf) {
        boolean isAuth = authService.checkSession(channel.id().toString());
        if (!isAuth) {
            if (code == OperationTypes.LOGIN.getCode()) {
                System.out.println("Login operation request is received");
                return new LoginHandler(authService, buf, channel);
            }
            System.out.println("Error: User is unauthorized");

            buf.clear();

            return null;
        }
        if (code == OperationTypes.DOWNLOAD.getCode()) {
            System.out.println("DOWNLOAD operation request is received");
            return new DownloadHandler(channel, root, buf);
        }
        if (code == OperationTypes.UPLOAD.getCode()) {
            System.out.println("UPLOAD operation request is received");
            return new UploadHandler(root, buf);
        }
        if (code == OperationTypes.SHOW.getCode()) {
            System.out.println("SHOW operation request is received");
            return new ShowHandler(channel, root, buf);
        }

        System.out.println("ERROR: Invalid first byte: " + code);
        return null;
    }
}
