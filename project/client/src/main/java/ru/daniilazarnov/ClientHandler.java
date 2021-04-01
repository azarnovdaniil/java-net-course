package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;

import java.util.Scanner;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static Logger log = Logger.getLogger(ClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("[Client]: Channel Active!!!");
        Thread t1 = new Thread(() -> {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();
                switch (msg) {
                    case "-test":
                        log.info(ctx.name());
                    case "-exit":
                        log.info("exit");
                        ctx.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Неизвестная комманда");
                }
            }
        });
        t1.start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            log.info("[Client]: Message received " + msg);
        } finally {
            ReferenceCountUtil.release(msg);
            //ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("[Client] : Error " + cause.getMessage(), cause);
        ctx.close();
    }
}
