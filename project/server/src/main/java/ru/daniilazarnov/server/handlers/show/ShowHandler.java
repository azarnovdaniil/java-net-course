package ru.daniilazarnov.server.handlers.show;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import ru.daniilazarnov.common.handlers.Handler;
import ru.daniilazarnov.common.OperationTypes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ShowHandler implements Handler {

    private static final int OPERATION_CODE_BYTES = 1;
    private static final int PATHS_SIZE = 4;
    private static final int PATH_LENGTH_SIZE = 4;

    private ChannelHandlerContext ctx;
    private ByteBuf buf;
    private String root;

    public ShowHandler(ChannelHandlerContext ctx, String root) {
        this.ctx = ctx;
        this.root = root;
    }

    @Override
    public void handle() throws IOException {

        Path rootPath = Paths.get(root);
        List<String> paths = Files.walk(rootPath).filter(Files::isRegularFile).map(Path::toString).
                collect(Collectors.toList());

        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(OPERATION_CODE_BYTES);
        buf.writeByte(OperationTypes.SHOW.getCode());
        ctx.channel().writeAndFlush(buf);

        buf = ByteBufAllocator.DEFAULT.directBuffer(PATHS_SIZE);
        buf.writeInt(paths.size());
        ctx.channel().writeAndFlush(buf);

        for (String path : paths) {
            byte[] pathBytes = path.getBytes(StandardCharsets.UTF_8);

            buf = ByteBufAllocator.DEFAULT.directBuffer(PATH_LENGTH_SIZE);
            buf.writeInt(pathBytes.length);
            ctx.channel().writeAndFlush(buf);

            buf = ByteBufAllocator.DEFAULT.directBuffer(pathBytes.length);
            buf.writeBytes(pathBytes);
            ctx.channel().writeAndFlush(buf);
        }
    }


    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public void setBuffer(ByteBuf buf) {
        this.buf = buf;
    }
}
