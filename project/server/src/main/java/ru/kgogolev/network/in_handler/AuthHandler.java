package ru.kgogolev.network.in_handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import ru.kgogolev.user.ListOfUsers;

import java.nio.charset.StandardCharsets;

public class AuthHandler extends ChannelInboundHandlerAdapter {


    private boolean authOk = false;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("клиент подключился");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (authOk) {
            ctx.fireChannelRead(msg);
            return;
        }
        ByteBuf inBuffer = (ByteBuf) msg;
        System.out.println(inBuffer.toString());
        String input = inBuffer.toString(CharsetUtil.UTF_8);
        System.out.println("Server received: " + input);
        if (input.split(" ")[0].equals("/auth")) {
            String username = input.split(" ")[1];
            if (ListOfUsers.USERS.get(input.split(" ")[1]).equals(input.split(" ")[2])) {
                authOk = true;
                System.out.println("auth OK");
                ctx.writeAndFlush(Unpooled.wrappedBuffer(("U have authorised as ".getBytes(StandardCharsets.UTF_8)),
                        username.getBytes(StandardCharsets.UTF_8)));
            } else {
                System.out.println("no such user");
                ctx.writeAndFlush(Unpooled.wrappedBuffer("Invalid login or password".getBytes(StandardCharsets.UTF_8),
                        username.getBytes(StandardCharsets.UTF_8)));
            }
        }
//        if (input.split(" ")[0].equals(StringConstants.DOWNLOAD)){
//            ctx.writeAndFlush(msg);
//        }
    }

}
