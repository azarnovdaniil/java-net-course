package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;

// Идет после FirstHandler в конвеере
public class SecondHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // Мы точно знаем, что предыдущий обработчик отдает нам массив байт
        byte[] arr = (byte[])msg;
        // Каждый элемент массива увеличиваем на 1
        for (int i = 0; i < 3; i++) {
            arr[i]++;
        }
        System.out.println("Второй шаг: " + Arrays.toString(arr));
        // Кидаем полученный массив дальше по конвееру
        ctx.fireChannelRead(arr);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
