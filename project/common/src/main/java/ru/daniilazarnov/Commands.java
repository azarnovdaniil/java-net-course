package ru.daniilazarnov;

import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Commands {

    public void listFiles(ChannelHandlerContext ctx) {
        File dir = new File("D://testDir");
        if(dir.isDirectory()){
            for(File item : dir.listFiles()){
                if(item.isDirectory()){
                    ctx.writeAndFlush(item.getName() + " \t / folder \n");
                }
                else{
                    ctx.writeAndFlush(item.getName() + "\t / file \n");
                }
            }
        }
    }

    public void uploadFile(ChannelHandlerContext ctx, String msg) throws IOException {
        String fileName = msg.split("\\s")[1];
        String fileText = msg.split("\\s", 3)[2];
        byte[] arr = fileText.getBytes();
        Files.write(Paths.get("D:/testDir/" + fileName), arr);
        ctx.writeAndFlush("File created: " + fileName);
    }
}