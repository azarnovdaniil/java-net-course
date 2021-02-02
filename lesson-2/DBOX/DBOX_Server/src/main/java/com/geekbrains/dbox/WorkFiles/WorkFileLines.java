package com.geekbrains.dbox.WorkFiles;

import com.geekbrains.dbox.WorkFiles.Data_File.JobInFiles;
import com.geekbrains.dbox.WorkFiles.Data_File.MainFileClass;
import com.geekbrains.dbox.WorkFiles.Data_File.WorkFileLinesMod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class WorkFileLines extends MainFileClass implements JobInFiles {
    private WorkFileLinesMod modWriter = WorkFileLinesMod.NEW;
    private int strUp, strDown; //Использеутся с модефикатором RANGE
    private int strEnd = -1; //Использеутся с модефикатором NEXT

    public WorkFileLines(String pathNameFile) {
        super(pathNameFile);
    }

    public int getStrEnd() {
        return strEnd;
    }

    public void setStrEnd(int strEnd) {
        this.strEnd = strEnd;
    }

    public int getStrUp() {
        return strUp;
    }

    public void setStrUp(int strUp) {
        this.strUp = strUp;
    }

    public int getStrDown() {
        return strDown;
    }

    public void setStrDown(int strDown) {
        this.strDown = strDown;
    }

    public WorkFileLinesMod getModWriter() {
        return modWriter;
    }

    public void setModWriter(WorkFileLinesMod modWriter) {
        this.modWriter = modWriter;
    }

    @Override
    public void writerInFile(Object wStr) throws IOException {
        String tStr = (String) wStr;
        File file = new File(this.pathNameFile);
        fileExists(file);

        switch (modWriter) {
            case NEW -> {// Перезапись файла
                PrintWriter pw = new PrintWriter(file);
                pw.println(tStr);
                pw.close();
            }
            case DOWN ->{// Добовление строки в конец файла
                PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                pw.println(tStr);
                pw.close();
            }
        }
    }

    @Override
    public Object readInFile() throws IOException {
        Path path = Paths.get(this.pathNameFile);
        Scanner scanner = new Scanner(path);
        Object ret = null; //Возвращаемый обьект
        //построчно считываем файл
        scanner.useDelimiter(System.getProperty("line.separator"));
        int countLines = countLinesOld(this.pathNameFile);//количество строк читаемого файла
        int numberStr = 0;//счетчик строк
        Object tmp = null;

        switch (this.modWriter) {
            case UP -> {//Считываем первую строку
                 ret = scanner.next();
                scanner.close();

                strEnd = 0;// Записываем что считали первую строку в файле
                this.modWriter = WorkFileLinesMod.NEXT;//меняем метот обхода файла
            }
            case ALL -> {//Считываем все строки в один массив
                String[] tmpStr = new String[countLines];

                while (scanner.hasNext()) {
                    tmpStr[numberStr] = scanner.next();
                    numberStr++;
                }
                scanner.close();
                ret = tmpStr;
            }
            case NEXT ->{//Считываем следующую строку
                while (scanner.hasNext()) {
                    if (this.strEnd + 1 <= countLines) { //Проверка не выйдет ли за предел след. считываемая строка
                        if (numberStr == this.strEnd + 1) {
                            ret = scanner.next();
                            strEnd = numberStr;
                            break;
                        }else tmp = scanner.next();
                    }else ret = null;
                    numberStr++;
                }
                scanner.close();
            }
            case RANGE -> {
                int coutnOb =  this.strDown - this.strUp; //Количество элементов массива tmpStr
                int indexCountOb = 0; //текущий индекс массива tmpStr
                String[] tmpStr = new String[coutnOb + 1];

                while (scanner.hasNext()) {
                    if((numberStr >= this.strUp)&&(numberStr <= this.strDown)){
                        tmpStr[indexCountOb] = scanner.next();
                        indexCountOb++;
                    } else tmp = scanner.next();
                    numberStr++;
                }
                scanner.close();
                ret = tmpStr;
            }
            case NEW -> {
                System.out.println("Индетефикатор NEW не для чтения файла");
            }
            case DOWN -> {
                System.out.println("Индетефикатор DOWN не для чтения файла");
            }
        }
        return ret;
    }
}
