package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;

import static io.netty.buffer.Unpooled.wrappedBuffer;

public class RepoEncoder extends ChannelOutboundHandlerAdapter {

    private final ContextData data;
    private static final Logger LOGGER = LogManager.getLogger(RepoEncoder.class);

    RepoEncoder(ContextData data) {
        this.data = data;
    }

    /**
     * Encodes the package to send it between Client and server. First part - command number, than all other data
     * from DataContext, augmented with it's length parameter. Last is the file chunk or other content to send and
     * a delimiter to separate packages in the stream.
     * @param ctx - current channel.
     * @param msg - package content. File chunks, mostly.
     */

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        byte[] command = ByteBuffer.allocate(4).putInt(data.getCommand()).array();
        byte[] chunk = null;
        if (data.getCommand() == CommandList.upload.getNum()) {
            chunk = new byte[((ByteBuf) msg).readableBytes()];
            ((ByteBuf) msg).readBytes(chunk);
        } else {
            chunk = (byte[]) msg;
        }
        byte[] delimiter = this.data.getDelimArray();
        ByteBuf encodedMessage = wrappedBuffer(
                command,
                encodeString(this.data.getFilePath()),
                encodeString(this.data.getLogin()),
                encodeString(this.data.getPassword()),
                chunk,
                delimiter
        );
        LOGGER.info("Byte package encoded and flushed.");
        ctx.writeAndFlush(encodedMessage);
    }

    private byte[] encodeString(String source) {
        byte[] message = source.getBytes();
        byte[] prefixMessage = ByteBuffer.allocate(4 + message.length).putInt(message.length).array();
        System.arraycopy(message, 0, prefixMessage, 4, message.length);
        return prefixMessage;
    }

}
