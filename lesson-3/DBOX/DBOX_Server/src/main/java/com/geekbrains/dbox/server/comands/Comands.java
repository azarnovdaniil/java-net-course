package com.geekbrains.dbox.server.comands;

import com.geekbrains.dbox.WorkFiles.WorkDir;

public class Comands {
    WorkDir wd = new WorkDir();

    public Comands(String rootDir){
        wd.setDirRoot(rootDir);
    }

    public String[] comand_Ls (String path){
        return wd.ls(path);
    }

    public boolean comand_Rename (String old_name, String new_name){
        return wd.rename(old_name, new_name);
    }

    public boolean comand_Delete (String path){
        return wd.delete(path);
    }

    public boolean comand_NewDir (String nameDir){
        return wd.createDir(nameDir);
    }
}
