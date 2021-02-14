//ДОМАШНЯЯ РАБОТА 6 --> сторона сервера  здесь 

Сторона клиента  и common уже загружена в папке project --> https://github.com/CAlina95/java-net-course/tree/main/project



import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.ChannelFuture;

import io.netty.channel.ChannelInitializer;

import io.netty.channel.ChannelOption;

import io.netty.channel.EventLoopGroup;

import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.SocketChannel;

import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.logging.Level;


public class Server {
    
		public static void main(String[] args) {
        
				Log.startLog();
        
				try {
         
				 new Server().run();
       
			 } catch (Exception e) {
        
				Log.protocolLogger.log(Level.WARNING, "Сервер не запускается :( Пожалуйста попробуйте еще раз!");
        
				Log.echoLogger.log(Level.WARNING, "Сервер не запускается :( Пожалуйста попробуйте еще раз!");
       
			 }

    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new Handler(), new EchoHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = b.bind(8189).sync();
            Database.connect();
            Log.protocolLogger.log(Level.INFO, "Сервер запущен!");
            Log.echoLogger.log(Level.INFO, "Сервер запущен!");
            future.channel().closeFuture().sync();
        } finally {
            Database.disconnect();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}



import java.io.IOException;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.logging.FileHandler;

import java.util.logging.Formatter;

import java.util.logging.Level;

import java.util.logging.LogRecord;

public class Log {
   
	 public static final java.util.logging.Logger protocolLogger = java.util.logging.Logger.getLogger(Handler.class.getName());
   
	 public static final java.util.logging.Logger echoLogger = java.util.logging.Logger.getLogger(EchoHandler.class.getName());

   
	 public static void startLog() {
        protocolLogger.setLevel(Level.CONFIG);
        echoLogger.setLevel(Level.CONFIG);
        try {
            java.util.logging.Handler protocolLogHandler = new FileHandler("info_protocol", true);
            java.util.logging.Handler echoLogHandler = new FileHandler("echo_info_protocol", true);
            protocolLogHandler.setLevel(Level.CONFIG);
            echoLogHandler.setLevel(Level.CONFIG);
            protocolLogger.addHandler(protocolLogHandler);
            echoLogger.addHandler(echoLogHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        protocolLogger.getHandlers()[0].setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date date = new Date();
                return dateFormat.format(date) + "\tmsg:" + record.getMessage() + "\tlvl: " + record.getLevel() + "\n";
            }
        });

        echoLogger.getHandlers()[0].setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date date = new Date();
                return dateFormat.format(date) + "\tmsg:" + record.getMessage() + "\tlvl: " + record.getLevel() + "\n";
            }
        });
    }
}



import io.netty.channel.ChannelHandlerContext;

import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.BufferedOutputStream;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.OutputStream;

import java.nio.file.*;

import java.sql.SQLException;

import java.util.logging.Level;

public class Handler extends ChannelInboundHandlerAdapter {
  
	@Override
   
	 public void channelRead(ChannelHandlerContext ctx, Object msg) {
       
			 ByteBuf buf = ((ByteBuf) msg);

        byte firstByte = buf.readByte();
        if (firstByte == Commands.getCommandLogin()) {
            Log.protocolLogger.log(Level.INFO, "Начать авторизацию");
            authorization(ctx, buf);
        }
        if (firstByte == Commands.getCommandUpload()) {
            Log.protocolLogger.log(Level.INFO, "Начать загружать");
            uploading(ctx, buf);
        }
        if (firstByte == Commands.getCommandDownload()) {
            Log.protocolLogger.log(Level.INFO, "Начать скачивать");
            downloading(ctx, buf);
        }
        if (firstByte == Commands.getCommandDelete()) {
            Log.protocolLogger.log(Level.INFO, "Начать удалять");
            deleting(ctx, buf);
        }
        if (firstByte == Commands.getCommandView()) {
            Log.protocolLogger.log(Level.INFO, "Начать просмотр");
            viewing(ctx, buf);
        }
    }

    private void authorization(ChannelHandlerContext ctx, ByteBuf buf) {
        short loginSize = buf.readShort();
        byte[] loginBytes = new byte[loginSize];
        buf.readBytes(loginBytes);
        String login = new String(loginBytes);
        short passwordSize = buf.readShort();
        byte[] passBytes = new byte[passwordSize];
        buf.readBytes(passBytes);
        String password = new String(passBytes);
        String name;
        try {
            name = Database.getNameByLogAndPass(login, password);
            if (name != null) {
                byte[] nameBytes = name.getBytes();
                byte[] resBytes = new byte[nameBytes.length + 1];
                resBytes[0] = Commands.getCommandLogin();
                System.arraycopy(nameBytes, 0, resBytes, 1, nameBytes.length);
                Log.protocolLogger.log(Level.CONFIG, name + " существует , доступ разрешен");
                ctx.channel().writeAndFlush(resBytes);
            } else {
                Log.protocolLogger.log(Level.CONFIG, "Имя не найдено");
                ctx.channel().writeAndFlush(new byte[]{Commands.getNameNotFound()});
            }
        } catch (SQLException e) {
            Log.protocolLogger.log(Level.WARNING, "Обнаружено SQL exception");
            e.printStackTrace();
        } finally {
            ctx.close();
        }
    }

    private void uploading(ChannelHandlerContext ctx, ByteBuf buf) {
        short nameSize = buf.readShort();
        byte[] nameBytes = new byte[nameSize];
        buf.readBytes(nameBytes);
        String name = new String(nameBytes);
        try {
            if (Database.isLogin(name)) {
                Log.protocolLogger.log(Level.INFO, "Вход подтвержден");
                short fileNameSize = buf.readShort();
                byte[] fileNameBytes = new byte[fileNameSize];
                buf.readBytes(fileNameBytes);
                String fileName = new String(fileNameBytes);
                if (!Files.exists(Paths.get("server_files/" + name))) {
                    Files.createDirectories(Paths.get("server_files/" + name));
                    Log.protocolLogger.log(Level.INFO, "Папка создана");
                }
                long size = buf.readLong();
                try (OutputStream out = new BufferedOutputStream(new FileOutputStream("server_files/" + name + "/" + fileName))) {
                    for (int i = 0; i < size; i++) {
                        out.write(buf.readByte());
                    }
                    Log.protocolLogger.log(Level.INFO, "Загрузка успешна");
                    ctx.channel().writeAndFlush(new byte[]{Commands.getCommandUpload()});
                }
            } else {
                Log.protocolLogger.log(Level.WARNING, "Обнаружен выход из системы");
                ctx.channel().writeAndFlush(new byte[]{Commands.getLogOut()});
            }
        } catch (SQLException | IOException e) {
            Log.protocolLogger.log(Level.WARNING, "Обнаружено исключение");
            e.printStackTrace();
        } finally {
            ctx.close();
        }
    }

    private void downloading(ChannelHandlerContext ctx, ByteBuf buf) {
        short nameSize = buf.readShort();
        byte[] nameBytes = new byte[nameSize];
        buf.readBytes(nameBytes);
        String name = new String(nameBytes);
        try {
            if (Database.isLogin(name)) {
                Log.protocolLogger.log(Level.INFO, "Вход подтвержден");
                short fileNameSize = buf.readShort();
                byte[] fileNameBytes = new byte[fileNameSize];
                buf.readBytes(fileNameBytes);
                String fileName = new String(fileNameBytes);
                try {
                    if (Files.exists(Paths.get("server_files/" + name + "/" + fileName))) {
                        Log.protocolLogger.log(Level.INFO, "Файл найден");
                        byte[] fileBytes = Files.readAllBytes(Paths.get("server_files/" + name + "/" + fileName));
                        byte[] resBytes = new byte[fileBytes.length + 1];
                        resBytes[0] = Commands.getCommandDownload();
                        System.arraycopy(fileBytes, 0, resBytes, 1, fileBytes.length);
                        ctx.channel().writeAndFlush(resBytes);
                        Log.protocolLogger.log(Level.INFO, "Файл отправлен");
                    } else {
                        Log.protocolLogger.log(Level.INFO, "Файл не найден");
                        ctx.channel().writeAndFlush(new byte[]{Commands.getNotFound()});
                    }
                } catch (IOException e) {
                    Log.protocolLogger.log(Level.WARNING, "IOException обнаружено");
                    e.printStackTrace();
                }
            } else {
                Log.protocolLogger.log(Level.WARNING, "Обнаружен выход из системы");
                ctx.channel().writeAndFlush(new byte[]{Commands.getLogOut()});
            }
        } catch (SQLException e) {
            Log.protocolLogger.log(Level.WARNING, "Обнаружено SQL exception");
            e.printStackTrace();
        } finally {
            ctx.close();
        }
    }

    private void deleting(ChannelHandlerContext ctx, ByteBuf buf) {
        short nameSize = buf.readShort();
        byte[] nameBytes = new byte[nameSize];
        buf.readBytes(nameBytes);
        String name = new String(nameBytes);
        try {
            if (Database.isLogin(name)) {
                Log.protocolLogger.log(Level.INFO, "Вход подтвержден");
                short fileNameSize = buf.readShort();
                byte[] fileNameBytes = new byte[fileNameSize];
                buf.readBytes(fileNameBytes);
                String fileName = new String(fileNameBytes);
                if (Files.exists(Paths.get("server_files/" + name + "/" + fileName))) {
                    Files.delete(Paths.get("server_files/" + name + "/" + fileName));
                    Log.protocolLogger.log(Level.INFO, "Файл найден и удален");
                    ctx.channel().writeAndFlush(new byte[]{Commands.getCommandDelete()});
                } else {
                    Log.protocolLogger.log(Level.INFO, "Файл не найден");
                    ctx.channel().writeAndFlush(new byte[]{Commands.getNotFound()});
                }
            } else {
                Log.protocolLogger.log(Level.WARNING, "Обнаружен выход из системы");
                ctx.channel().writeAndFlush(new byte[]{Commands.getLogOut()});
            }
        } catch (SQLException | IOException e) {
            Log.protocolLogger.log(Level.WARNING, "Обнаружено исключение");
            e.printStackTrace();
        } finally {
            ctx.close();
        }
    }

    private void viewing(ChannelHandlerContext ctx, ByteBuf buf) {
        short nameSize = buf.readShort();
        byte[] nameBytes = new byte[nameSize];
        buf.readBytes(nameBytes);
        String name = new String(nameBytes);
        try {
            if (Database.isLogin(name)) {
                Log.protocolLogger.log(Level.INFO, "Вход подтвержден");
                if (Files.exists(Paths.get("server_files/" + name))) {
                    StringBuilder sb = new StringBuilder();
                    Path dir = Paths.get("server_files/" + name);
                    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                        for (Path file : stream) {
                            sb.append(file.getFileName()).append(";");
                        }
                    } catch (IOException | DirectoryIteratorException e) {
                        e.printStackTrace();
                    }
                    String resultLine = sb.toString().trim();
                    byte[] bytes = resultLine.getBytes();
                    if (bytes.length != 0) {
                        Log.protocolLogger.log(Level.INFO, "Файл найден в папке");
                        byte[] resBytes = new byte[bytes.length + 1];
                        resBytes[0] = Commands.getCommandView();
                        System.arraycopy(bytes, 0, resBytes, 1, bytes.length);
                        ctx.channel().writeAndFlush(resBytes);
                    } else {
                        Log.protocolLogger.log(Level.INFO, "В папке пусто");
                        ctx.channel().writeAndFlush(new byte[]{Commands.getEmpty()});
                    }
                } else {
                    Log.protocolLogger.log(Level.INFO, "Папка не найдена");
                    ctx.channel().writeAndFlush(new byte[]{Commands.getEmpty()});
                }
            } else {
                Log.protocolLogger.log(Level.WARNING, "Обнаружен выход из системы");
                ctx.channel().writeAndFlush(new byte[]{Commands.getLogOut()});
            }
        } catch (SQLException e) {
            Log.protocolLogger.log(Level.WARNING, "Обнаружено SQL exception");
            e.printStackTrace();
        } finally {
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}



import io.netty.buffer.ByteBuf;

import io.netty.channel.ChannelHandlerContext;

import io.netty.channel.ChannelOutboundHandlerAdapter;

import io.netty.channel.ChannelPromise;

import java.util.logging.Level;

public class EchoHandler extends ChannelOutboundHandlerAdapter {
   
	 @Override
  
	public void write (ChannelHandlerContext ctx, Object msg, ChannelPromise promise){

        byte[] bytes = (byte[]) msg;

        if (bytes[0] == Commands.getCommandLogin()) {
            Log.echoLogger.log(Level.INFO, "Команда принята [Log In]");
        }

        if (bytes[0] == Commands.getEmpty()) {
            Log.echoLogger.log(Level.INFO, "Команда принята[EMPTY]");
            returnBytes(ctx, "Пусто".getBytes());
        }
        if (bytes[0] == Commands.getCommandUpload()) {
            Log.echoLogger.log(Level.INFO, "Команда принята [Upload]");
            returnBytes(ctx, "Успешно загружено".getBytes());
        }
        if (bytes[0] == Commands.getCommandDownload()) {
            Log.echoLogger.log(Level.INFO, "Команда принята [Download]");
            returnBytes(ctx, bytes);
        }
        if (bytes[0] == Commands.getCommandView() || bytes[0] == Commands.getCommandLogin()) {
            if (bytes[0] == Commands.getCommandView()) {
                Log.echoLogger.log(Level.INFO, "Команда принята [View]");
            }

            byte[] answer = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, answer, 0, bytes.length - 1);
            returnBytes(ctx, answer);
        }
        if (bytes[0] == Commands.getCommandDelete()) {
            Log.echoLogger.log(Level.INFO, "Команда принята [Delete]");
            returnBytes(ctx, "Успешно удалено".getBytes());
        }
        if (bytes[0] == Commands.getNotFound()) {
            Log.echoLogger.log(Level.INFO, "Команда принята [Not Found]");
            returnBytes(ctx, "Данный файл отсутствует".getBytes());
        }
        if (bytes[0] == Commands.getNameNotFound()) {
            Log.echoLogger.log(Level.INFO, "Команда принята [Name Not Found]");
            returnBytes(ctx, new byte[] {Commands.getNameNotFound()});
        }
        if (bytes[0] == Commands.getLogOut()) {
            Log.echoLogger.log(Level.INFO, "Команда принята [Log Out]");
            returnBytes(ctx, new byte[]{Commands.getLogOut()});
        }
    }

    private void returnBytes(ChannelHandlerContext ctx, byte[] bytes) {
        ByteBuf buf = ctx.alloc().buffer(bytes.length);
        buf.writeBytes(bytes);
        ctx.writeAndFlush(buf);
        Log.echoLogger.log(Level.INFO, "Данные успешно отправлены");
        ctx.close();
        buf.release();
    }
}
