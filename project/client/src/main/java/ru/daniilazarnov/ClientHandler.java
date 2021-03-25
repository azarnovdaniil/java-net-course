package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

import java.net.SocketAddress;
import java.util.Scanner;

public class ClientHandler extends ChannelOutboundHandlerAdapter {


    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {



            ctx.writeAndFlush("hello");

        }
    }

