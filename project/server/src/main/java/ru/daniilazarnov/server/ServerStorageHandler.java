package ru.daniilazarnov.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.daniilazarnov.util.file.FileUtilImpl;

public class ServerStorageHandler extends ChannelInboundHandlerAdapter {
  private FileUtilImpl fileUtil;

  public ServerStorageHandler() {
    this.fileUtil = new FileUtilImpl();
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ByteBuf in = (ByteBuf) msg;
    try {
      while (in.isReadable()) {
        System.out.print((char) in.readByte());
        System.out.flush();
      }
    } finally {
      ReferenceCountUtil.release(msg);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
