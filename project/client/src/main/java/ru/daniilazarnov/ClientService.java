package ru.daniilazarnov;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import ru.daniilazarnov.util.command.CommandUtilImpl;
import ru.daniilazarnov.util.exception.IncorrectCommandException;
import ru.daniilazarnov.util.exception.IncorrectFileNameException;
import ru.daniilazarnov.util.interaction.Message;

public class ClientService {
  private final Network network;

  public ClientService() throws IOException {
    CountDownLatch networkStarter = new CountDownLatch(1);
    this.network = new Network();

    initClient(new CommandUtilImpl());
    network.start(networkStarter);
  }

  private void initClient(CommandUtilImpl c) {
    Thread listening = new Thread(() -> {
      while (true) {
        Scanner scanner = new Scanner(System.in);
        String inputString = scanner.nextLine();
        try {
          c.doCommand(inputString);
        } catch (IncorrectCommandException | IOException | IncorrectFileNameException e) {
          e.printStackTrace();
        }
      }
    });

    listening.start();
  }

  public void run(Path path) throws IOException {

//    sendFile(path, future -> {
//      if (!future.isSuccess()) {
//        future.cause().printStackTrace();
//        network.stop();
//      }
//      if (future.isSuccess()) {
//        System.out.println("Файл успешно передан");
//        network.stop();
//      }
//    });
  }

  private void sendFile(Path path, ChannelFutureListener finishListener) throws IOException {
    Channel channel = network.getCurrentChannel();

    FileRegion region = new DefaultFileRegion(path.toFile(), 0, Files.size(path));

    ByteBuf buf;
    buf = ByteBufAllocator.DEFAULT.directBuffer(1);
    buf.writeByte((byte) 25);
    network.getCurrentChannel().writeAndFlush(buf);

    byte[] filenameBytes = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
    buf = ByteBufAllocator.DEFAULT.directBuffer(4);
    buf.writeInt(filenameBytes.length);
    channel.writeAndFlush(buf);

    buf = ByteBufAllocator.DEFAULT.directBuffer(filenameBytes.length);
    buf.writeBytes(filenameBytes);
    channel.writeAndFlush(buf);

    buf = ByteBufAllocator.DEFAULT.directBuffer(8);
    buf.writeLong(Files.size(path));
    channel.writeAndFlush(buf);

    ChannelFuture transferOperationFuture = channel.writeAndFlush(region);
    if (finishListener != null) {
      transferOperationFuture.addListener(finishListener);
    }
  }

  private void sendMessage() {
    Channel channel = network.getCurrentChannel();

    network.getCurrentChannel().writeAndFlush(new Message("Hello"));
  }
}
