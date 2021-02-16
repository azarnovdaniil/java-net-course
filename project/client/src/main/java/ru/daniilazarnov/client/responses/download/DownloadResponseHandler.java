package ru.daniilazarnov.client.responses.download;

import io.netty.buffer.ByteBuf;
import ru.daniilazarnov.common.files.FileReader;
import ru.daniilazarnov.common.files.ReadState;
import ru.daniilazarnov.common.handlers.Handler;
import java.io.IOException;

public class DownloadResponseHandler implements Handler {

    private ByteBuf buf;

    private final FileReader fileReader;

    public DownloadResponseHandler(String downloadsRoot) {
        fileReader = new FileReader(downloadsRoot);
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
