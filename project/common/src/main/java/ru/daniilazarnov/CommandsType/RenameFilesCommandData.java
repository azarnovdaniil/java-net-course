package ru.daniilazarnov.CommandsType;

import java.io.Serializable;

public class RenameFilesCommandData implements Serializable {


        private final String oldName;
        private final String newName;

    public RenameFilesCommandData(String oldName, String newName) {
            this.oldName = oldName;
            this.newName = newName;
        }

        public String getOldName() {

            return oldName;
        }

        public String getNewName() {

            return newName;
        }

}

