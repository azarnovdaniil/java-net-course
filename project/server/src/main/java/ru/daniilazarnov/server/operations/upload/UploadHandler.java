package ru.daniilazarnov.server.operations.upload;

import io.netty.buffer.ByteBuf;
import ru.daniilazarnov.common.files.FileReader;
import ru.daniilazarnov.common.files.ReadState;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.common.handlers.HandlerException;

import java.io.*;

public class UploadHandler implements Handler {

    private final FileReader fileReader;
    private ByteBuf buf;

    public UploadHandler(String root, ByteBuf buf) {
        fileReader = new FileReader(root);
        this.buf = buf;
    }

    @Override
    public void handle() throws HandlerException {
        try {
            fileReader.readFile(buf);
        } catch (IOException e) {
            throw new HandlerException("Exception has been occurred via uploading the file", e);
        }
    }

    @Override
    public boolean isComplete() {
        return fileReader.getState() == ReadState.COMPLETE;
    }
}
