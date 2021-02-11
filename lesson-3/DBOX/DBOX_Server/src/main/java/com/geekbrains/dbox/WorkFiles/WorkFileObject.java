package com.geekbrains.dbox.WorkFiles;

import com.geekbrains.dbox.WorkFiles.Data_File.JobInFiles;
import com.geekbrains.dbox.WorkFiles.Data_File.MainFileClass;

import java.io.*;

public class WorkFileObject extends MainFileClass implements JobInFiles {
    WorkFileObject(String pathNameFile) {
        super(pathNameFile);
    }

    public void writerInFile(Object[] wStr) throws IOException {
        writerInFile(wStr);
    }
    @Override
    public void writerInFile(Object wStr) throws IOException {
        fileExists(new File(this.pathNameFile));
        FileOutputStream fos = new FileOutputStream(this.pathNameFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(wStr);

        fos.close();
    }

    @Override
    public Object readInFile() throws IOException, ClassNotFoundException {

        FileInputStream fis = null;
        fis = new FileInputStream(this.pathNameFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return ois.readObject();
    }
}
