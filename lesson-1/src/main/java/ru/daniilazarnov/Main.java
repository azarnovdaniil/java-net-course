package ru.daniilazarnov;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        String s = "hello world!";
//        try (FileOutputStream fos = new FileOutputStream("D://testDir//note.txt")){
//            byte [] buf = s.getBytes();
//            fos.write(buf, 0, buf.length);
//            System.out.println("write done!");
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//        File dir1 = new File("D://testDir//newDir//");
//        dir1.mkdir();
//        System.out.println(dir1.getAbsoluteFile());
//        try (FileInputStream fis=new FileInputStream("D://testDir//10.mp3");
//             FileOutputStream fos=new FileOutputStream("D://testDir//newDir//11.mp3")){
//
//            byte[] buffer = new byte[fis.available()];
//            fis.read(buffer, 0, buffer.length);
//            fos.write(buffer, 0 , buffer.length);
//        } catch (IOException e){
//            e.printStackTrace();
//        }
        File dir = new File("D://testDir");
        if(dir.isDirectory())
        {
            // получаем все вложенные объекты в каталоге
            for(File item : dir.listFiles()){

                if(item.isDirectory()){

                    System.out.println(item.getName() + "  \t folder");
                }
                else{

                    System.out.println(item.getName() + "\t file");
                }
            }
        }
    }
}
