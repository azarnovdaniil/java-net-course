package ru.daniilazarnov.server.operations.show;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import ru.daniilazarnov.common.CommonPackageConstants;
import ru.daniilazarnov.common.commands.Commands;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.common.handlers.HandlerException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ShowHandler implements Handler {

    private Channel channel;
    private String root;

    public ShowHandler(Channel channel, String root) {
        this.channel = channel;
        this.root = root;
    }

    @Override
    public void handle() throws HandlerException {

        Path rootPath = Paths.get(root);
        List<String> paths = null;
        try {
            paths = Files.walk(rootPath).filter(Files::isRegularFile).map(rootPath::relativize).
                    map(Path::toString).
                    collect(Collectors.toList());
        } catch (IOException e) {
            throw new HandlerException("Exception has been occurred via reading files in the user's catalog", e);
        }

        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1);
        buf.writeByte(Commands.SHOW.getCode());
        channel.writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(CommonPackageConstants.CONTENT_LENGTH_BYTES.getCode());
        buf.writeInt(paths.size());
        channel.writeAndFlush(buf);

        for (String path : paths) {
            byte[] pathBytes = path.getBytes(StandardCharsets.UTF_8);

            buf = ByteBufAllocator.DEFAULT.directBuffer(CommonPackageConstants.CONTENT_LENGTH_BYTES.getCode());
            buf.writeInt(pathBytes.length);
            channel.writeAndFlush(buf);

            buf = ByteBufAllocator.DEFAULT.directBuffer(pathBytes.length);
            buf.writeBytes(pathBytes);
            channel.writeAndFlush(buf);
        }
    }


    @Override
    public boolean isComplete() {
        return true;
    }

}
