package ru.daniilazarnov.CommandsType;

import java.io.Serializable;
import java.util.List;

public class DownloadFilesCommandData implements Serializable {


        private final List<String> fileName;

        public DownloadFilesCommandData(List<String> fileName) {
            this.fileName = fileName;
        }

        public List<String> getFileName() {
            return fileName;
        }

}

