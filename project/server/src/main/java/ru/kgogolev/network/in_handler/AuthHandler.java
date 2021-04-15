package ru.kgogolev.network.in_handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import ru.kgogolev.StringUtil;
import ru.kgogolev.user.ListOfUsers;

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
        if (StringUtil.getWordFromLine(input, 0).equals("/auth")) {
            String username = StringUtil.getWordFromLine(input, 1);
            if (ListOfUsers.USERS.get(StringUtil.getWordFromLine(input, 1))
                    .equals(StringUtil.getWordFromLine(input, 2))) {
                authOk = true;
                System.out.println("auth OK");
                ctx.writeAndFlush(StringUtil.lineToByteBuf("U have authorised as " + username));
            } else {
                System.out.println("no such user");
                ctx.writeAndFlush(StringUtil.lineToByteBuf("Invalid login or password " + username));
            }
        }

    }

}
