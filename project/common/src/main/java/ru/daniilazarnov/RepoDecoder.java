package ru.daniilazarnov;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static io.netty.buffer.Unpooled.wrappedBuffer;

public class RepoDecoder extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Message recieved");
        //byte[] input = ((String)msg).getBytes();
        //System.out.println(Arrays.toString(input));
        //ByteBuf k = wrappedBuffer(input);
        ByteBuf k = (ByteBuf)msg;
        int command = k.readInt();
        System.out.println("Клманда " +command);
        int length = k.readInt();
        byte[] nameBytes = new byte[length];
        for (int i = 0; i < length; i++) {
            nameBytes[i]=k.readByte();
        }
        String name = new String(nameBytes, StandardCharsets.UTF_8);
        System.out.println(name);
        byte[] bytes= new byte[k.readableBytes()];
        k.readBytes(bytes);
        k.release();

        if (command==CommandList.upload.ordinal()) {
            System.out.println("Загрузка файла");
            FileContainer container = new FileContainer(bytes,name);
            ctx.fireChannelRead(container);
        }else if(command==CommandList.delete.ordinal()){
            System.out.println("Удаление файла");
            Path test = Paths.get("server\\src\\main\\java\\ru\\daniilazarnov\\" + name);
            if (Files.exists(test)){
            Files.delete(test);
            if (!Files.exists(test))System.out.println("Файл удален");
            }else System.out.println("Файл не найден");

        } else System.out.println("Неизвестная команда");

    }
}
