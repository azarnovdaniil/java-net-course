package ru.daniilazarnov;

import java.io.Serializable;
import java.nio.file.Path;

public class File implements Serializable {
    private static final long serialVersionUID = 5193392663743561680L;

    private Object object;

    public File(String msg) {
    }

    public Object getFile() {
        return object;
    }

    public File (Path object) {
        this.object = object;
    }
}
