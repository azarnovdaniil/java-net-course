package ru.sviridovaleksey.newclientconnection;

import io.netty.channel.ChannelHandlerContext;
import ru.sviridovaleksey.Command;

public class MessageForClient {

    private final String name = "server";

    private final ChannelHandlerContext ctx;

   public MessageForClient(ChannelHandlerContext ctx) {
       this.ctx = ctx;
   }

   public void err(String message) {
       ctx.writeAndFlush(Command.errActionMessage(name, message));
   }

    public void message(String message) {
        ctx.writeAndFlush(Command.message("server", message));
    }

   public void successfulAction(String message) {
       ctx.writeAndFlush(Command.successAction(name, message));
   }

   public void responseShowDirectory(String message) {
       ctx.writeAndFlush(Command.showAllInDirectory(name, message));
   }

   public void sendCommandForClient(Command command) {
       ctx.writeAndFlush(command); }
}
