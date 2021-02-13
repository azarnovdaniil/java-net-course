package ru.daniilazarnov.CommandsType;

import java.io.Serializable;

public class DeleteFilesCommandData implements Serializable {

       private final String fileName;


       public DeleteFilesCommandData (String fileName) {

            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
       }
}

