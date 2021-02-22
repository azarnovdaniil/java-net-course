package ru.daniilazarnov.CommandsType;

import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;

public class DeleteFilesCommandData implements Serializable {


       private String login;
       private LinkedList<File> fileNameToDelete;


       public DeleteFilesCommandData (String login, LinkedList<File> fileNameToDelete) {

            this.login = login;
            this.fileNameToDelete = fileNameToDelete;
        }

        public String getLogin(){
           return login;
        }
        public LinkedList<File> getFileNameToDelete() {

           return fileNameToDelete;
       }
}

