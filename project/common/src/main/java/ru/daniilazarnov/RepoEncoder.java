package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static io.netty.buffer.Unpooled.wrappedBuffer;

public class RepoEncoder extends ChannelOutboundHandlerAdapter {
    private ContextData data;
    RepoEncoder(ContextData data){
        this.data=data;
    }
    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        super.read(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        byte[] command = ByteBuffer.allocate(4).putInt(data.getCommand()).array();
        byte[] name = data.getFilePath().getBytes();
        byte[] length = ByteBuffer.allocate(4).putInt(name.length).array();
        System.out.println("name length "+name.length);
        byte[] mess=null;
        if (data.getCommand()==CommandList.upload.ordinal()) {
           mess = new byte[((ByteBuf) msg).readableBytes()];
            ((ByteBuf) msg).readBytes(mess);
        }

        if (data.getCommand()==CommandList.delete.ordinal()){
           mess = (byte[])msg;
        }
        byte[] del= new byte[data.getDelimiter().readableBytes()];
        System.out.println("sgsdgsg"+data.getDelimiter().readableBytes());
        data.getDelimiter().readBytes(del);
        data.getDelimiter().resetReaderIndex();
        ByteBuf wrappedBuffer = wrappedBuffer(command,length,name,mess,del);
        System.out.println(wrappedBuffer.readableBytes());
        ctx.writeAndFlush(wrappedBuffer);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        super.flush(ctx);
    }
}
