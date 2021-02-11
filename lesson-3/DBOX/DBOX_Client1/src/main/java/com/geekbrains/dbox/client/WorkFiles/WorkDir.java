package com.geekbrains.dbox.client.WorkFiles;

import java.io.File;
import java.util.ArrayList;

import static java.io.File.separatorChar;

public class WorkDir {
    private String dirRoot = "";

    public void setDirRoot(String dirRoot) {
        this.dirRoot = dirRoot;
    }

    public String getDirRoot() {
        return dirRoot;
    }

    public String[] ls(String dir){
        String[] arrAll = null;
        String path = dirRoot;
        if(!dir.equals("")) path = dirRoot + separatorChar + dir;

        File f = new File(path);
        arrAll = f.list();

        return arrAll;
    }

    public String[] getAll(String dir){
        String[] arrAll = null;
        String path = dirRoot;
        if(!dir.equals("")) path = dirRoot + separatorChar + dir;

        arrAll = gAll(path);
        return arrAll;
    }
    public String[] getAll_All(String dir){
        ArrayList<String> tempArr = new ArrayList<>();
        String[] arrAll = null;
        String path = dirRoot;
        if(!dir.equals("")) path = dirRoot + separatorChar + dir;

        System.out.println("\"" + path + "\"");
        String[] allF = gAll(path);
        addStr(tempArr, allF);

        for (int i = 0; i < allF.length; i++) {
            if(!isFile(allF[i])){
                String[] tStr = gAll_All(allF[i]);
                addStr(tempArr, tStr);
            }
        }
        arrAll = new String[tempArr.size()];
        for (int i = 0; i < arrAll.length; i++) {
            arrAll[i] = tempArr.get(i);
        }


        return arrAll;
    }
    public String[] getTreeDir(String dir) {
        String[] arrDir = null;
        String path = dirRoot;
        if(!dir.equals("")) path = dirRoot + separatorChar + dir;

        File dirR = new File(path);
        File[] paths = dirR.listFiles();
        boolean t = false;
        int colDir = 0;

        for (int i = 0; i < paths.length; i++) {
            if (paths[i].isFile()) t = true;
            else {
                t = false;
                colDir++;
            }
        }

        arrDir = new String[colDir];
        int indexDir = 0;

        for (int i = 0; i < paths.length; i++) {
            if (paths[i].isFile()) t = true;
            else {
                t = false;
                arrDir[indexDir] = paths[i].getPath();
                indexDir++;
            }
        }
        return arrDir;
    }
    public String[] getTreeDir_All(String dir){
        ArrayList<String> tempArr = new ArrayList<>();
        String[] arrAll = null;
        String path = dirRoot;
        if(!dir.equals("")) path = dirRoot + separatorChar + dir;

        System.out.println("\"" + path + "\"");
        String[] allF = gAll(path);
        addStrD(tempArr, allF);

        for (int i = 0; i < allF.length; i++) {
            if(!isFile(allF[i])){
                String[] tStr = gAll_All(allF[i]);
                addStrD(tempArr, tStr);
            }
        }
        arrAll = new String[tempArr.size()];
        for (int i = 0; i < arrAll.length; i++) {
            arrAll[i] = tempArr.get(i);
        }


        return arrAll;
    }
    public String[] getTreeFile(String dir){
        String[] arrFile = null;
        String path = dirRoot;
        if(!dir.equals("")) path = dirRoot + separatorChar + dir;

        File dirR = new File(path);
        File[] paths = dirR.listFiles();
        boolean t = false;
        int colFiles = 0;

        for (int i = 0; i < paths.length; i++) {
            if (paths[i].isFile()) {
                t = true;
                colFiles++;
            }
            else {
                t = false;
            }
        }

        arrFile = new String[colFiles];
        int indexFile = 0;

        for (int i = 0; i < paths.length; i++) {
            if (paths[i].isFile()) {
                t = true;
                arrFile[indexFile] = paths[i].getPath();
                indexFile++;
            }
            else {
                t = false;
            }
        }

        return arrFile;
    }
    public String[] getTreeFile_All(String dir){
        ArrayList<String> tempArr = new ArrayList<>();
        String[] arrAll = null;
        String path = dirRoot;
        if(!dir.equals("")) path = dirRoot + separatorChar + dir;

        System.out.println("\"" + path + "\"");
        String[] allF = gAll(path);
        addStrF(tempArr, allF);

        for (int i = 0; i < allF.length; i++) {
            if(!isFile(allF[i])){
                String[] tStr = gAll_All(allF[i]);
                addStrF(tempArr, tStr);
            }
        }
        arrAll = new String[tempArr.size()];
        for (int i = 0; i < arrAll.length; i++) {
            arrAll[i] = tempArr.get(i);
        }


        return arrAll;
    }
    public boolean createDir(String dir){
        if (dir.equals("")) return false;
        boolean res = false;
        File file = new File(dirRoot + separatorChar + dir);
        res = file.mkdir();
        return res;
    }
    public boolean delete(String dir){
        if (dir.equals("")) return false;
        boolean res = false;
        File file = new File(dirRoot + separatorChar + dir);
        res = file.delete();
        return res;
    }
    public boolean rename(String dir, String newName){
        if (dir.equals("")) return false;
        boolean res = false;
        File file = new File(dirRoot + separatorChar + dir);
            res = file.renameTo(new File(newName));
        return res;
    }

    private boolean isFile(String path){
        boolean res = false;
        return  res = new File(path).isFile();
    }
    private String[] gAll(String path){
        String[] arrAll = null;

        File dirR = new File(path);
        File[] paths = dirR.listFiles();
        boolean t = false;
        int colDir = 0;

        for (int i = 0; i < paths.length; i++) {
            colDir++;
        }

        arrAll = new String[colDir];
        int indexDir = 0;

        for (int i = 0; i < paths.length; i++) {
            arrAll[indexDir] = paths[i].getPath();
            indexDir++;
        }
        return arrAll;
    }
    private String[] gAll_All(String path){
       // System.out.println(">gAll_All>\"" + path + "\"<<");
        String[] arrAll = null;
        int colF = 0;
        String[] paths = gAll(path);
        if (paths.length != 0) {
            colF = paths.length + 1;

            for (int i = 0; i < paths.length; i++) {
                if (!isFile(paths[i])) {
                    String[] tempArr = gAll_All(paths[i]);
                    if(tempArr != null) colF = colF + tempArr.length + 1;
                }
            }
            arrAll = new String[colF];
            int idexArrAll = 0;
            //System.out.println(arrAll.length + "<arrAll");
            for (int i = 0; i < paths.length; i++) {
                arrAll[idexArrAll] = paths[i];
                idexArrAll++;
            }

            for (int i = 0; i < paths.length; i++) {
                if (!isFile(paths[i])) {
                    String[] tempArr = gAll_All(paths[i]);
                    if(tempArr != null) {
                        for (int j = 0; j < tempArr.length; j++) {
                            //System.out.println(">tempArr>\"" + tempArr[j] + " | " + idexArrAll + "\"<<");
                            arrAll[idexArrAll] = tempArr[j];
                            idexArrAll++;
                        }
                    }
                }
            }
        }else arrAll = null;


        return arrAll;
    }
    private void addStr (ArrayList<String> arrayList, String[] arrStr){
        for (int i = 0; i < arrStr.length; i++) {
            if(arrStr[i] != null) arrayList.add(arrStr[i]);
        }
    }
    private void addStrF (ArrayList<String> arrayList, String[] arrStr){
        for (int i = 0; i < arrStr.length; i++) {
            if(arrStr[i] != null) if(isFile(arrStr[i])) arrayList.add(arrStr[i]);
        }
    }
    private void addStrD (ArrayList<String> arrayList, String[] arrStr){
        for (int i = 0; i < arrStr.length; i++) {
            if(arrStr[i] != null) if(!isFile(arrStr[i])) arrayList.add(arrStr[i]);
        }
    }
}

