package ru.daniilazarnov.server_app;

// обработчик серверных команд

import ru.daniilazarnov.Command;
import ru.daniilazarnov.entity.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Commands {

    public enum State {
        IDLE, NAME_LENGTH, NAME, FILE_LENGTH, FILE
    }

    public State getCurrentState() {
        return currentState;
    }

    private State currentState = State.IDLE;
    private int nextLength;
    private long fileLength;
    private long receivedFileLength;
    private BufferedOutputStream out;

public void toLogIn(ByteBuf buff, ServerApp serverApp, ClientHandler clientHandler, ChannelHandlerContext ctx){
    ByteBuf mes = ctx.alloc().buffer();
    String[] credentialValues =buff.toString(StandardCharsets.UTF_8).split("\\s");
    User user = serverApp.getAuthenticationService().doAuth(credentialValues[1], credentialValues[2]);
    if(user != null){
        if (!clientHandler.isLoggedIn(user.getNickname())) {
            clientHandler.subscribe(ctx.channel(),user);
            System.out.println("Авторизовался клиент " + clientHandler.getName(ctx.channel()));
            mes.writeBytes("Auth OK".getBytes(StandardCharsets.UTF_8));
        } else {
            mes.writeBytes("Current user is already logged in.".getBytes(StandardCharsets.UTF_8));
        }
    }else {
        mes.writeBytes("No a such user by email and password.".getBytes(StandardCharsets.UTF_8));
    }
    ctx.writeAndFlush(mes);
}

    public void toRegister(ByteBuf buff, ServerApp serverApp, ChannelHandlerContext ctx) throws IOException {
        ByteBuf mes = ctx.alloc().buffer();
        String[] credentialValuesReg =buff.toString(StandardCharsets.UTF_8).split("\\s");
        if(serverApp.getAuthenticationService().doReg(credentialValuesReg[1], credentialValuesReg[2])){
            mes.writeBytes("Such user is already registered".getBytes(StandardCharsets.UTF_8));
        }else{
            Path userDirectory = Paths.get("/NetworkStorage/"+credentialValuesReg[1]);
            if(!Files.exists(userDirectory)){
                Files.createDirectory(userDirectory);
            }
            serverApp.getAuthenticationService()
                    .addUser(new User(credentialValuesReg[1], credentialValuesReg[2],userDirectory));
            mes.writeBytes("Reg OK".getBytes(StandardCharsets.UTF_8));
            System.out.println("Зарегистрировался клиент " + credentialValuesReg[1]);
        }
        ctx.writeAndFlush(mes);
    }

    public void toLS(ClientHandler clientHandler, ChannelHandlerContext ctx) throws IOException {
        ByteBuf mes = ctx.alloc().buffer();
        Files.walk(clientHandler.getUser(ctx.channel()).getCurrentPath(), FileVisitOption.FOLLOW_LINKS)
                .map(Path::toFile)
                .forEach(f -> mes.writeBytes((f.getAbsolutePath() + (f.isDirectory() ? " каталог" : " файл") +'\n').getBytes(StandardCharsets.UTF_8)));
        ctx.writeAndFlush(mes);
    }

    public void toMkDir(ByteBuf buff, ClientHandler clientHandler, ChannelHandlerContext ctx) throws IOException {
        ByteBuf mes = ctx.alloc().buffer();
        String[] credentialValuesDir =buff.toString(StandardCharsets.UTF_8).split("\\s");
        Path p = Paths.get(clientHandler.getUser(ctx.channel()).getCurrentPath().toString(),credentialValuesDir[1]);
        System.out.println(p);
        if(!Files.exists(p)){
            Files.createDirectory(p);
        }
        mes.writeBytes(("Директория " + credentialValuesDir[1] + " создана").getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(mes);
    }

    public void toShowHelp(ChannelHandlerContext ctx) throws IOException {
        ByteBuf mes = ctx.alloc().buffer();
        File file = new File("..\\java-net-course\\project\\common\\src\\main\\java\\help");
        InputStream fis = new FileInputStream(file);
        mes.writeBytes(fis.readAllBytes());
        ctx.writeAndFlush(mes);
        fis.close();
    }

    public void toCD(ByteBuf buff, ClientHandler clientHandler, ChannelHandlerContext ctx) {
        ByteBuf mes = ctx.alloc().buffer();
        if (mes.readableBytes() > 0) {
            String[] cd =buff.toString(StandardCharsets.UTF_8).split("\\s");
            System.out.println(cd[1]);
            Path path = Paths.get(clientHandler
                    .getUser(ctx.channel())
                    .getCurrentPath()+cd[1]);
            File f = new File(String.valueOf(path));
            if(f.isDirectory()){
                clientHandler.getUser(ctx.channel()).setCurrentPath(path);
            }
            else {
                mes.writeBytes("Путь указан неверно".getBytes(StandardCharsets.UTF_8));
                return;
            }
            mes.writeBytes(("Вы перешли в " + path.toString()).getBytes(StandardCharsets.UTF_8));
        }
        else {
            Path changedDirectory = clientHandler
                    .getUser(ctx.channel())
                    .getCurrentPath()
                    .getParent();
            clientHandler.getUser(ctx.channel()).setCurrentPath(changedDirectory);
            mes.writeBytes(("Вы перешли в " + changedDirectory.toString()).getBytes(StandardCharsets.UTF_8));
        }
        ctx.writeAndFlush(mes);
    }

    public void toUpLoad(ClientHandler clientHandler, ByteBuf buff, ChannelHandlerContext ctx) throws IOException {
        ByteBuf mes = ctx.alloc().buffer();
        while (buff.readableBytes() > 0) {
            if (currentState == State.IDLE) {
                currentState = State.NAME_LENGTH;
                receivedFileLength = 0L;
                System.out.println("STATE: Start file receiving");
            }

            if (currentState == State.NAME_LENGTH) {
                if (buff.readableBytes() >= 4) {
                    System.out.println("STATE: Get filename length");
                    nextLength = buff.readInt();
                    currentState = State.NAME;
                }
            }

            if (currentState == State.NAME) {
                if (buff.readableBytes() >= nextLength) {
                    byte[] fileName = new byte[nextLength];
                    buff.readBytes(fileName);
                    System.out.println("STATE: Filename received - " + new String(fileName, StandardCharsets.UTF_8));
                    //out = new BufferedOutputStream(new FileOutputStream("_" + new String(fileName)));
                    System.out.println(clientHandler.getUser(ctx.channel()).getCurrentPath() + new String(fileName, StandardCharsets.UTF_8));
                    File file = new File(clientHandler.getUser(ctx.channel()).getCurrentPath() +"\\"+ new String(fileName, StandardCharsets.UTF_8));
                    out = new BufferedOutputStream(new FileOutputStream(file));
                    currentState = State.FILE_LENGTH;
                }
            }

            if (currentState == State.FILE_LENGTH) {
                if (buff.readableBytes() >= 8) {
                    fileLength = buff.readLong();
                    System.out.println("STATE: File length received - " + fileLength);
                    currentState = State.FILE;
                }
            }

            if (currentState == State.FILE) {
                while (buff.readableBytes() > 0) {
                    out.write(buff.readByte());
                    receivedFileLength++;
                    if (fileLength == receivedFileLength) {
                        currentState = State.IDLE;
                        System.out.println("File received");
                        mes.writeBytes("Файл загружен в хранилище".getBytes());
                        ctx.writeAndFlush(mes);
                        out.close();
                        break;
                    }
                }
            }

        }
    }

    public void toDownload(ClientHandler clientHandler, ByteBuf buff, ChannelHandlerContext ctx) throws IOException {
        Path path = Paths.get(clientHandler.getUser(ctx.channel()).getCurrentPath() +"\\"+ buff.toString(StandardCharsets.UTF_8).split("\\s")[1]);
        FileRegion region = new DefaultFileRegion(path.toFile(), 0, Files.size(path));
        byte[] filenameBytes = path.getFileName().toString().getBytes(StandardCharsets.UTF_8);
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1+4+filenameBytes.length + 8);
        buf.writeByte(Command.DOWNLOAD.getSignal());
        buf.writeInt(filenameBytes.length);
        buf.writeBytes(filenameBytes);
        buf.writeLong(Files.size(path));
        ctx.writeAndFlush(buf);
        ctx.writeAndFlush(region);
    }

    public void toRemove(ClientHandler clientHandler, ByteBuf buff, ChannelHandlerContext ctx){
        ByteBuf mes = ctx.alloc().buffer();
        File file = new File(clientHandler.getUser(ctx.channel()).getCurrentPath() +"\\"+ buff.toString(StandardCharsets.UTF_8).split("\\s")[1]);
        if(file.delete()){
            mes.writeBytes("файл удален".getBytes());
        }else mes.writeBytes("Файла не обнаружено".getBytes());
        ctx.writeAndFlush(mes);
    }

    public void toRename(ClientHandler clientHandler, ByteBuf buff, ChannelHandlerContext ctx){
        ByteBuf mes = ctx.alloc().buffer();
        File file = new File(clientHandler.getUser(ctx.channel()).getCurrentPath() +"\\"+ buff.toString(StandardCharsets.UTF_8).split("\\s")[1]);
        File newFile = new File(clientHandler.getUser(ctx.channel()).getCurrentPath() +"\\"+ buff.toString(StandardCharsets.UTF_8).split("\\s")[2]);
        if(file.renameTo(newFile)){
            mes.writeBytes("Файл переименован успешно".getBytes());
        }else{
            mes.writeBytes("Файл не был переименован".getBytes());
        }
        ctx.writeAndFlush(mes);
    }

}
