package ru.daniilazarnov;

import ru.daniilazarnov.files.FileSerializable;
import ru.daniilazarnov.oper.Operations;

import java.io.Serializable;
import java.util.Map;

public class ObjectSerialization implements Serializable {
    private Operations oper;
    private FileSerializable file;
    private Map<String, String> dir;

    public ObjectSerialization(Operations oper, FileSerializable file, Map<String, String> dir) {
        this.oper = oper;
        this.file = file;
        this.dir = dir;
    }

    public Operations getOper() {
        return oper;
    }

    public FileSerializable getFile() {
        return file;
    }

    public Map<String, String> getDir() {
        return dir;
    }
}