package ru.daniilazarnov.server.operations;

import io.netty.channel.Channel;
import ru.daniilazarnov.common.OperationTypes;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.server.operations.download.DownloadHandler;
import ru.daniilazarnov.server.operations.show.ShowHandler;
import ru.daniilazarnov.server.operations.upload.UploadHandler;

public class ServerOperationsFactory {

    public Handler createOperation(byte code, Channel channel, String root) {
        if (code == OperationTypes.DOWNLOAD.getCode()) {
            System.out.println("DOWNLOAD operation request is received");
            return new DownloadHandler(channel, root);
        } else if (code == OperationTypes.UPLOAD.getCode()) {
            System.out.println("UPLOAD operation request is received");
            return new UploadHandler(root);
        } else if (code == OperationTypes.SHOW.getCode()) {
            System.out.println("SHOW operation request is received");
            return new ShowHandler(channel, root);
        } else if (code == OperationTypes.LOGIN.getCode()) {
            System.out.println("LOGIN operation request is received");
            return new ShowHandler(channel, root);
        } else {
            System.out.println("ERROR: Invalid first byte: " + code);
            return null;
        }
    }

}
