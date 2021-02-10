package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.daniilazarnov.database.DBService;

public class ServerDBHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof DBMessage) {
            DBService dbService = new DBService();
            DBMessage receivedRequest = (DBMessage) msg;
            String dbCmd = receivedRequest.getCmd();

            switch (dbCmd) {
                case "reg":
                    dbService.addUser(receivedRequest.getLogin(), receivedRequest.getPassword());
                    System.out.println("hello its me debug");
                    break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
