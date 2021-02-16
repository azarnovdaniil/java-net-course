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
       ctx.write(Command.errActionMessage(name, message));
   }

   public void successfulAction(String message) {
       ctx.write(Command.successAction(name, message));
   }

   public void responseShowDirectory(String message) {
       ctx.write(Command.showAllInDirectory(name, message));
   }

   public void sendCommandForClient(Command command) {
       ctx.write(command); }
}
