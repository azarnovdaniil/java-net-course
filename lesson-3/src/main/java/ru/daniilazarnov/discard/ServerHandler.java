package ru.daniilazarnov.discard;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;



    public class ServerHandler extends SimpleChannelInboundHandler<String>{

        private static final ChannelGroup CHANNELS = new DefaultChannelGroup(new DefaultEventExecutor());

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        incoming.writeAndFlush("[SERVER] - welcome to chat!\r\n");

        for (Channel channel : CHANNELS) {
            channel.writeAndFlush("[" + incoming.remoteAddress() + "] has joined!\r\n");
        }
        CHANNELS.add(incoming);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel removed = ctx.channel();

        for (Channel channel : CHANNELS) {
            channel.write("[" + removed.remoteAddress() + "] has remover chat!\r\n");
            channel.flush();
            channel.writeAndFlush(ctx);
        }
        CHANNELS.remove(removed);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel talk = ctx.channel();
        System.out.println("channelRead0 [" + talk.remoteAddress() + "] " + msg);

        for (Channel channel : CHANNELS) {
            if (channel.remoteAddress() != talk.remoteAddress()){
                channel.writeAndFlush("[" + talk.remoteAddress() + "] " + msg + "\r\n");
            } else {
                channel.writeAndFlush("[Client] " + msg + "\r\n");
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel talk = ctx.channel();

        String str_msg = msg.toString();
        System.out.println(str_msg);
        String sd = "";
        if ((str_msg.charAt(0) == '[') && (str_msg.charAt(str_msg.length() - 1) == ']')) {

            //Определение имени файла для записи
            String[] strname = talk.remoteAddress().toString()
                    .replace('/', ' ').replace(':', ' ')
                    .trim().split(" ");
            String nameFile = "C:\\java_log\\out_file\\" + strname[0] + " copy.txt";

            //Подготовка данных для записи в файл
            String[] str = msg.toString().replace("[", " ").replace("]", " ").trim().split(", ");
            System.out.println("channelRead [" + talk.remoteAddress() + "] ");
            System.out.println(nameFile);

            File dest;
            FileOutputStream fileOS;
            FileWriter fw;

//            OutputStreamWriter outSW = new OutputStreamWriter(fileOS, "windows-1251");

            //Создание файла, если он не существует
            if (!new File(nameFile).exists()){
                System.out.println(new File(nameFile).exists());
                dest = new File(nameFile);
                fileOS = new FileOutputStream(dest);
            }

            //Запись в файл
            for (int i = 0; i < str.length; i++) {
                //Перевод числового значения символа в символ
                char c = (char) Integer.parseInt(String.valueOf(Integer.parseInt(str[i])));
                System.out.print(c);
                sd += c;
//                outSW.append(c);

            }
            Files.write(Paths.get(nameFile), sd.getBytes(), StandardOpenOption.APPEND);
//            outSW.close();
            System.out.println("");
            talk(ctx, sd);
        } else {
            System.out.println("channelRead [" + talk.remoteAddress() + "] " + msg);
            talk(ctx, msg);
        }
    }

    public void talk(ChannelHandlerContext ctx, Object msg) {
        Channel talk = ctx.channel();
        for (Channel channel : CHANNELS) {
            if (channel.remoteAddress() != talk.remoteAddress()){
                channel.writeAndFlush("[" + talk.remoteAddress() + "] " + msg + "\r\n");
            } else {
                channel.writeAndFlush("[Client] " + msg + "\r\n");
            }
        }
    }

    public void send(String msg) {
        for (Channel channel : CHANNELS) {
            channel.writeAndFlush("[SERVER] " + msg + "\r\n");
        }
    }
}