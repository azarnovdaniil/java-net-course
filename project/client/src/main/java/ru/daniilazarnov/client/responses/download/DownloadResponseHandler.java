package ru.daniilazarnov.client.responses.download;

import io.netty.buffer.ByteBuf;
import ru.daniilazarnov.common.files.FileReader;
import ru.daniilazarnov.common.files.ReadState;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.common.handlers.HandlerException;

import java.io.IOException;

public class DownloadResponseHandler implements Handler {

    private ByteBuf buf;

    private final FileReader fileReader;

    public DownloadResponseHandler(String downloadsRoot, ByteBuf buf) {
        fileReader = new FileReader(downloadsRoot);
        this.buf = buf;
    }

    @Override
    public void handle() throws HandlerException {
        try {
            fileReader.readFile(buf);
        } catch (IOException e) {
            throw new HandlerException("Exception has been occurred via reading the file from the server", e);
        }
    }

    @Override
    public boolean isComplete() {
        return fileReader.getState() == ReadState.COMPLETE;
    }

}
