package ru.daniilazarnov;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PathInfo {
    List<FileInfo> infoList = new ArrayList<>();
    Path path;

    PathInfo(Path path){
        this.path = path;
    }

    public void updateList() throws IOException {


     //  List<File> f = Files.walk(this.path,FileVisitOption.FOLLOW_LINKS).map(FileInfo::new).collect(Collectors.toList());
       Files.walk(path, FileVisitOption.FOLLOW_LINKS)
                .map(Path::toFile)
                .forEach(file ->
                {
                    infoList.add(new FileInfo(Path.of(file.getAbsolutePath())));
                    System.out.println(new FileInfo(Path.of(file.getAbsolutePath())).toString());
                }  );

    }

    public List<FileInfo> getInfoList() {
        return infoList;
    }

    @Override
    public String toString() {
        return "PathInfo{" +
                "infoList=" + infoList +
                '}';
    }
}

