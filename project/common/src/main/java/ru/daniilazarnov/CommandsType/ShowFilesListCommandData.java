package ru.daniilazarnov.CommandsType;

import java.io.Serializable;
import java.util.List;

public class ShowFilesListCommandData implements Serializable {

        private final List<String> files;

        public ShowFilesListCommandData(List<String> files) {
            this.files = files;
        }

        public List<String> getFiles() {
            return files;
        }


}

