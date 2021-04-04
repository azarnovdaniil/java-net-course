package ru.daniilazarnov;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class PathInfo {
    List<FileInfo> infoList;
    Path path;

    PathInfo(Path path){
        this.path = path;
    }

    public void updateList() throws IOException {


        //infoList.addAll(Files.list(this.path).map(FileInfo::new).collect(Collectors.toList()));
        Files.walk(Paths.get(String.valueOf(path)), FileVisitOption.FOLLOW_LINKS)
                .map(Path::toFile)
                .forEach(f -> {
                    System.out.println(f.getAbsolutePath() + (f.isDirectory() ? " каталог" : " файл"));
                });

    }

    public List<FileInfo> getInfoList() {
        return infoList;
    }
}

