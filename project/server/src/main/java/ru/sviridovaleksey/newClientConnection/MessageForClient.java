package ru.sviridovaleksey.newClientConnection;

import io.netty.channel.ChannelHandlerContext;
import ru.sviridovaleksey.Command;

public class MessageForClient {

    private ChannelHandlerContext ctx;

   public MessageForClient (ChannelHandlerContext ctx) {
       this.ctx = ctx;
   }

   public void errCreateFile (String message) {
       System.out.println(message);
       ctx.write(Command.errActionMessage("server", message));
   }

   public void successfulAction (String message) {
       ctx.write(Command.successAction("server", message));
   }


}
