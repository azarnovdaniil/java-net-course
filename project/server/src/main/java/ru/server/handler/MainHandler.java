package ru.server.handler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.*;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainHandler extends ChannelInboundHandlerAdapter { // (1)

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client connected...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Client disconnected...");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);
        String s = new String(data);
        String[] strings = s.split(" ");
        if (strings[0].equals("/help")){
        s = callHelpManual().toString();
        }
        else if (strings[0].equals("/show")){
            s = showFiles(Path.of("C:\\java\\serverLock")).toString();
        }
        else {
            System.out.println(s);
        }
        byte[] arr = s.getBytes();
        ByteBuf buf = ctx.alloc().buffer(arr.length);
        buf.writeBytes(arr);
        ctx.writeAndFlush(buf);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
    protected StringBuilder callHelpManual() {
        File f = new File("project/server/src/help.txt");
        BufferedReader fin = null;
        try {
            fin = new BufferedReader(new FileReader(f));
        StringBuilder sb = new StringBuilder();
        String line;
            while ((line = fin.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return sb;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    protected StringBuilder showFiles(Path path){
        File dir = new File(String.valueOf(path));
        StringBuilder sb = new StringBuilder();

            File[] files = dir.listFiles();
        sb.append(" For user: user " + "\n");
            for (File file : files) {
                    long lastModified = file.lastModified();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                    sb.append(file.getName() + " ,date of change " + sdf.format(new Date(lastModified))+"\n");

            }

            return sb;
    }
}