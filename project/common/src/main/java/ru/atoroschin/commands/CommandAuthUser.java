package ru.atoroschin.commands;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import ru.atoroschin.*;

import java.util.List;

public class CommandAuthUser implements CommandAuth {

    @Override
    public void send(ChannelHandlerContext ctx, String content, byte signal) {
        List<String> list = List.of(content);
        ByteBuf bufOut = BufWorker.makeBufFromList(list, signal);
        ctx.fireChannelRead(bufOut);
    }

    @Override
    public void response(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService,
                         FileWorker fileWorker, byte signal) {
        List<String> list = BufWorker.readFileListFromBuf(buf);
        String strID = list.get(0);
        try {
            int ID = Integer.parseInt(strID);
            String dir = authService.getUserFolder(ID);
            fileWorker.appendBasePath(authService.getUserFolder(ID));
            fileWorker.setMaxVolume(authService.getMaxVolume(ID));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, ByteBuf buf, AuthService authService) {

    }
}
