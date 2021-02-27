package ru.daniilazarnov.actual;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private ClientState state = ClientState.IDLE;
    private Commands command;
    private static final String ROOT = new File("").getAbsolutePath() + "\\";
    private String targetDir = ROOT;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Добро пожаловть на файловый сервер! Для получения справки отправьте help");
        command = new Commands();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        while (buf.readableBytes() > 0){
            if (state == ClientState.IDLE){
                byte signal = buf.readByte();

                if (signal == Signals.LS.get()){
                    state = ClientState.LS;
                    List list = Utils.convertByteBufToList(buf);
                    for (Object s : list){
                        System.out.println(s);
                    }
                    state = ClientState.IDLE;
                }

                else if (signal == Signals.CD.get()){
                    targetDir = buf.toString(StandardCharsets.UTF_8);
                    File file = new File(targetDir);
                    if (!file.exists()){
                        file.mkdirs();
                    }
                }

                else if (signal == Signals.TEXT.get()){
                    String str = buf.toString(StandardCharsets.UTF_8);
                    System.out.println(str);
                }

                else if (signal == Signals.START_UPLOAD.get()){
                    state = ClientState.TRANSFER;
                    command.receiveFile(buf, targetDir);
                }

            } else if (state == ClientState.TRANSFER){
                if (command.receiveFile(buf, targetDir)) {
                    state = ClientState.IDLE;
                    targetDir = ROOT;
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Connection to the server is lost");
        cause.printStackTrace();
        ctx.close();
    }
}
