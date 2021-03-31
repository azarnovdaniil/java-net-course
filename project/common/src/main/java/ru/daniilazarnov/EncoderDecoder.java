package ru.daniilazarnov;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

public class EncoderDecoder {

    public static class Encoder extends MessageToByteEncoder<String> {

        @Override
        protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out)
                throws Exception {
            System.out.println("Encode: " + msg);
            if (msg.length() == 0) {
                return;
            }
            Charset charset = Charsets.UTF_8;
            ByteBuf byteBuf = Unpooled.wrappedBuffer(msg.getBytes(charset));
            out.writeBytes(byteBuf);
        }
    }

    public static class Decoder extends ByteToMessageDecoder {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
            if(in.readableBytes() < 4) {
                return;
            }

            String msg = in.toString(CharsetUtil.UTF_8);
            in.readerIndex(in.readerIndex() + in.readableBytes());
            System.out.println("Decode:"+msg);

            out.add(msg);
        }

    }
}
