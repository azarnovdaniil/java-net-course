package ru.kgogolev.network.in_handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import ru.kgogolev.FileDecoder;
import ru.kgogolev.FileSystem;
import ru.kgogolev.StringConstants;
import ru.kgogolev.StringUtil;

public class ServerInputHandler extends ChannelInboundHandlerAdapter {
    private String currentDirectory;
    private final FileSystem fileSystem;
    private FileDecoder fileDecoder;

    public ServerInputHandler(String currentDirectory, FileSystem fileSystem, FileDecoder fileDecoder) {
        this.currentDirectory = currentDirectory;
        this.fileSystem = fileSystem;
        this.fileDecoder = fileDecoder;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        String input = buf.toString(CharsetUtil.UTF_8);
        if (input.split(" ")[0].equals(StringConstants.DOWNLOAD)) {
            ctx.writeAndFlush(msg);
        } else if (input.split(" ")[0].equals(StringConstants.VIEW_SERVER_FILES)) {
            ctx.writeAndFlush(
                    StringUtil.lineToByteBuf(fileSystem.walkFileTree(currentDirectory)));
        } else {
            fileDecoder.decodeFile(buf);
        }
        if (buf.readableBytes() == 0) {
            buf.release();
            ctx.writeAndFlush(Unpooled.copiedBuffer("File recieved", CharsetUtil.UTF_8));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился");
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
