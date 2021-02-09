package client;

import clientserver.Commands;
import clientserver.commands.CommandLS;
import clientserver.commands.CommandUpload;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ClientHandlerNetty extends SimpleChannelInboundHandler {
    private int partSize;

    public ClientHandlerNetty(int partSize) {
        super();
        this.partSize = partSize;
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
//        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("Netty Rocks!", CharsetUtil.UTF_8));
        consoleRead(channelHandlerContext);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("Пришло сообщение");

        ByteBuf buf = (ByteBuf) o;
        byte firstB = 0;
        if (buf.readableBytes() > 0) {
            firstB = buf.readByte();
        }
        switch (firstB) {
            case 1:
                // команда upload
                break;
            case 3:
                // команда ls
                List<String> list = CommandLS.readResponse(buf);
                System.out.println("Файлы: " + list);
                break;
            case 4:
                break;
        }
//        System.out.println(buf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        cause.printStackTrace();
        channelHandlerContext.close();
    }

    public boolean uploadFileNetty(ChannelHandlerContext chc, String fileName, byte[] bytes) throws IOException {

        File file = new File(fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        long volume = file.length();
        int countPar = (int) volume / partSize + 1;

        int lenServiceData = 1 + 4 + bytes.length;
        byte[] buffer = new byte[partSize];

        int readLen = fileInputStream.read(buffer, 0, partSize - lenServiceData);

//        lenServiceData += readLen;

//        long length = lenServiceData+readLen;
        ByteBuf bufOut = ByteBufAllocator.DEFAULT.buffer(partSize);
        bufOut.writeByte(Commands.UPLOAD.getSignal()); // команда
//        bufOut.writeLong(length); // длина сообщения
        bufOut.writeInt(lenServiceData + readLen); // длина сообщения
        bufOut.writeBytes(bytes); // служебные данные. все, кроме содержимого файла
        bufOut.writeBytes(buffer, 0, readLen); // содержимое файла
//        System.arraycopy(bytes, 0, buffer, 0, bytes.length);

//        Arrays.fill(buffer,(byte) 0);
//        bufOut.readBytes(buffer, 0, partSize-lenServiceData);


        chc.writeAndFlush(bufOut);

        System.out.println("отправлено байт: " + (readLen + lenServiceData));
//        volume -=readLen;
//        while (volume>0) {
//            readLen = fileInputStream.read(buffer, 0, partSize);
//            out.write(buffer,0,readLen);
//            out.flush();
//        }

//        bufOut.release();
        return true;
    }

    public void consoleRead(ChannelHandlerContext ctx) {
        Thread threadConsole = new Thread(() -> {
            try {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String readLine = "";
                    System.out.print("Введите команду: ");
                    if (scanner.hasNext()) {
                        readLine = scanner.nextLine();
                        String strCommand = readLine.contains(" ") ? readLine.split("\\s", 2)[0] : readLine;

                        Commands command = Commands.valueOf(strCommand.toUpperCase(Locale.ROOT));

                        command.sendToServer(ctx, readLine);
                        byte[] by;
                        switch (command) {
                            case UPLOAD:
//                                String[] param = readLine.split("\\s",2);
                                String fileName = readLine.contains(" ") ? readLine.split(" ")[1] : "";
                                // запаковать в массив
                                by = CommandUpload.makeResponse(fileName);
                                if (by != null) {
                                    uploadFileNetty(ctx, fileName, by);
                                }
                                break;
                            case LS:
                                ByteBuf byBuf = ByteBufAllocator.DEFAULT.buffer();
                                byBuf.writeByte(command.getSignal());
                                byBuf.writeInt(5);
                                ctx.writeAndFlush(byBuf);
                                break;
                            default:
                                System.out.println("Неизвестная команда");
                        }
                        TimeUnit.SECONDS.sleep(2);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        threadConsole.setDaemon(true);
        threadConsole.start();
    }
}
