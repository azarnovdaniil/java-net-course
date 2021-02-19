package ru.daniilazarnov.server.operations.upload;

import io.netty.buffer.ByteBuf;
import ru.daniilazarnov.common.files.FileReader;
import ru.daniilazarnov.common.files.ReadState;
import ru.daniilazarnov.common.handlers.Handler;
import java.io.*;

public class UploadHandler implements Handler {

    private final FileReader fileReader;
    private ByteBuf buf;

    public UploadHandler(String root) {
        fileReader = new FileReader(root);
    }

    @Override
    public void handle() throws IOException {
        fileReader.readFile(buf);
    }

    @Override
    public boolean isComplete() {
        return fileReader.getState() == ReadState.COMPLETE;
    }

    @Override
    public void setBuffer(ByteBuf buf) {
        this.buf = buf;
    }
}
