package ru.daniilazarnov.client.responses.show;

import io.netty.buffer.ByteBuf;
import ru.daniilazarnov.common.handlers.Handler;

import java.nio.charset.StandardCharsets;


public class ShowResponseHandler implements Handler {

    private static final int PATHS_SIZE = 4;
    private static final int PATH_LENGTH_SIZE = 4;

    private ShowHandlerState state = ShowHandlerState.LENGTH;
    private ByteBuf buf;

    private long pathsNum;
    private long currentPathsNum = 0;
    private int currentPathLength;

    public ShowResponseHandler() {
    }

    @Override
    public void handle() {
        if (state == ShowHandlerState.LENGTH) {
            if (buf.readableBytes() >= PATHS_SIZE) {
                pathsNum = buf.readInt();
                if (pathsNum > 0) {
                    System.out.println(pathsNum + " files in the storage:");
                    state = ShowHandlerState.PATH_LENGTH;
                } else {
                    state = ShowHandlerState.COMPLETE;
                }
            }
        }

        if (state == ShowHandlerState.PATH_LENGTH) {
            if (buf.readableBytes() >= PATH_LENGTH_SIZE) {
                currentPathLength = buf.readInt();
                state = ShowHandlerState.PATH;
            }
        }

        if (state == ShowHandlerState.PATH) {
            if (buf.readableBytes() >= currentPathLength) {
                byte[] pathBytes = new byte[currentPathLength];
                buf.readBytes(pathBytes);
                String path = new String(pathBytes, StandardCharsets.UTF_8);
                System.out.println(path);
                currentPathsNum++;
                if (currentPathsNum == pathsNum) {
                    state = ShowHandlerState.COMPLETE;
                } else {
                    state = ShowHandlerState.PATH_LENGTH;
                }
            }
        }
    }

    @Override
    public boolean isComplete() {
        return state == ShowHandlerState.COMPLETE;
    }

    @Override
    public void setBuffer(ByteBuf buf) {
        this.buf = buf;
    }
}
