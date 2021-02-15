package server;

import common.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.IOException;
import java.nio.file.Path;


public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static String userFolder = "src/main/java/server/fileUser1";
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = ((ByteBuf) msg);
        if (ReceivingFile.getCurrentState() == State.IDLE) {
            byte readed = buf.readByte();
            Commands commands = Commands.valueOf(readed);
            switch (commands) {
                case DOWNLOAD:
                    downloadFileFromServer(ctx, buf);
                    break;
            }
        }
        if (ReceivingFile.getCurrentState() == State.FILE) {
            uploadFileToServer(msg);
        }
    }
    private void downloadFileFromServer(ChannelHandlerContext ctx, ByteBuf buf) throws IOException {
        String fileName = ReceivingString.receiveAndEncodeString(buf);
        System.out.println("fileName ".toUpperCase() + fileName);

        System.out.println("STATE: Start file download");
        FileSender.sendFile(Path.of(userFolder, fileName),
                (io.netty.channel.Channel) ctx.channel(),
                Method.getChannelFutureListener("Файл успешно передан"));
        buf.clear();
    }
    private void uploadFileToServer(Object msg) throws IOException {
        ReceivingFile.fileReceive(msg, "user");
    }


}
