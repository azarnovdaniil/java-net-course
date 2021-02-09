package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = Logger.getLogger(ClientHandler.class);
    private static final String user ="user1";
    private State currentState = State.IDLE;

    public ClientHandler() {
    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        ReceivingFiles.fileReceive(msg, user);

//        ctx.fireChannelRead(buf);

        Client.printPrompt(); // вывод строки приглашения к вводу
    }


//        if (readed == (byte) 70) {
//            System.out.println("Принято сообщение 70 " + readed);
//        } else {
//            System.out.println("Принято сообщение " + readed);
//        }
//


//        System.out.println(("\n" + (String) msg));






    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        ctx.close();
        cause.printStackTrace();
    }
}
