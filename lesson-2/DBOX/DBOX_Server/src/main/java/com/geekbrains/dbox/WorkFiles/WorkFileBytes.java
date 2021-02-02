package com.geekbrains.dbox.WorkFiles;

import com.geekbrains.dbox.WorkFiles.Data_File.JobInFiles;
import com.geekbrains.dbox.WorkFiles.Data_File.MD5Checksum;
import com.geekbrains.dbox.WorkFiles.Data_File.MainFileClass;
import com.geekbrains.dbox.WorkFiles.Data_File.WorkFileLinesMod;

import java.io.*;

public class WorkFileBytes extends MainFileClass implements JobInFiles {
    private WorkFileLinesMod modWriter = WorkFileLinesMod.DOWN; //UP, DOWN (Перезапись, дозапись)
    int sizeReadeArrByte = 0; // количество считываемых байтов

    protected WorkFileBytes(String pathNameFile) {
        super(pathNameFile);
    }

    @Override //Запись в файл
    public void writerInFile(Object wStr) throws IOException {
        //wStr =  Массив байтов byte[]
        boolean selct = true;
        if (modWriter == WorkFileLinesMod.DOWN) selct = true; //дозапись в файл
        if (modWriter == WorkFileLinesMod.UP) selct = false; // перезапись файла

        try {
            FileOutputStream fos = new FileOutputStream(this.pathNameFile, selct);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            // конвертим
            byte[] buffer = (byte[]) wStr;

            bos.write(buffer, 0, buffer.length);
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }


    @Override//Ччтение маленьких файлов(за раз)
    public Object readInFile() throws IOException, ClassNotFoundException {
        byte[] arrByte = null;
        try {
            FileInputStream fis = new FileInputStream(this.pathNameFile);
            BufferedInputStream bis = new BufferedInputStream(fis);

                arrByte = bis.readAllBytes();

        }catch (Exception e){

            System.out.println(e.getMessage());
        }
        return arrByte;
    }
    //Чтение больших файлов(pos - позиция начала считывания, sizeReadeArrByte - Количество байтов для считывания)
    public Object readInFile(int pos){

        if(sizeReadeArrByte == 0) {
            System.out.print("Size buffer empty !!!!!!");
            return null;
        }

        byte[] arrByte = null;
        byte[] tempArrByte = new byte[sizeReadeArrByte];
        try {
            FileInputStream fis = new FileInputStream(this.pathNameFile);
            BufferedInputStream bis = new BufferedInputStream(fis);

            int col = bis.readNBytes(tempArrByte, pos, sizeReadeArrByte);
            if (col < tempArrByte.length) {
                arrByte = new byte[col];
                for (int i = 0; i < col; i++) {
                    arrByte[i] = tempArrByte[i];
                }}else{
                arrByte = tempArrByte;
            }

        }catch (Exception e){

            System.out.println(e.getMessage());
        }
        return arrByte;
    }

    public long getSizeFile(String path){
        long size = 0;
        File file = new File(path);
        if (file.isFile()){
            size = file.length();
        }else{
            System.out.print("\"" + path + "\" " + "Файл не существует" );
        }

        return size;
    }
    public String getCheckSummMD5 () throws Exception {
        String res = null;
        res = MD5Checksum.getMD5Checksum(this.pathNameFile);
        return res;
    }
}
