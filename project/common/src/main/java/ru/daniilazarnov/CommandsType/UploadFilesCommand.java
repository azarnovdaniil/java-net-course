package ru.daniilazarnov.CommandsType;

import java.io.Serializable;

public class UploadFilesCommand implements Serializable {

         private final String fileName;

         public UploadFilesCommand (String fileName) {

        this.fileName = fileName;
        }

         public String getFileName() {
         return fileName;
         }


}

