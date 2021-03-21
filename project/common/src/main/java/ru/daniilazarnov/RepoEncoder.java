package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import java.nio.ByteBuffer;
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
        byte[] name = data.getFileName().getBytes();
        byte[] length = ByteBuffer.allocate(4).putInt(name.length).array();
        System.out.println("name length "+name.length);
        byte[] mess=null;
        if (data.getCommand()==1) {
           mess = new byte[((ByteBuf) msg).readableBytes()];
            ((ByteBuf) msg).readBytes(mess);
        }

        if (data.getCommand()==2){
           mess = (byte[])msg;
        }
        ByteBuf wrappedBuffer = wrappedBuffer(command,length,name,mess);
        System.out.println(wrappedBuffer.readableBytes());
        ctx.writeAndFlush(wrappedBuffer);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        super.flush(ctx);
    }
}
