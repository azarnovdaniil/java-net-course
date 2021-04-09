package ru.daniilazarnov;

import helpers.ServerFileHelper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import messages.AuthMessage;
import messages.FileMessage;
import messages.Message;
import messages.MessageType;

import java.util.logging.Logger;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public ServerHandler(String username) {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;

        switch (message.getType()){
            case AUTHORIZATION:
                AuthMessage authMessage = (AuthMessage) message.getMessage();
                break;
            case REGISTRATION:
                AuthMessage regMessage = (AuthMessage) message.getMessage();
                break;
            case FILE_ACTION:
                FileMessage fm = (FileMessage) message.getMessage();

                switch (fm.getType()){

                    case UPLOAD:
                        ServerFileHelper.saveFile(ctx.channel(), msg, fm.getUser().getLogin(), fm.getPath1());
                        break;
                    case DOWNLOAD:
                        ServerFileHelper.getFile(ctx.channel(), fm.getUser().getLogin(), fm.getPath1());
                        break;
                    case RENAME:
                        ServerFileHelper.renameFile(fm.getUser().getLogin(), fm.getPath1(), fm.getPath2());
                        break;
                    case DELETE:
                        ServerFileHelper.deleteFile(fm.getUser().getLogin(), fm.getPath1());
                        break;
                    case LIST:
                        ServerFileHelper.listFilesInDirectory(fm.getUser().getLogin(), fm.getPath1());
                        break;
                    case SHARE:
                        ServerFileHelper.copyFile(fm.getPath1(), fm.getPath2());
                        break;
                }
                break;
        }
        ctx.writeAndFlush(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
