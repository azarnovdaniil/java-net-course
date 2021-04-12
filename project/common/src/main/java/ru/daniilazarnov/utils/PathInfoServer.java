package ru.daniilazarnov.utils;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PathInfoServer {
    List<FileInfoServer> infoList = new ArrayList<>();
    Path path;

    PathInfoServer(Path path) {
        this.path = path;
    }

    public void updateList() throws IOException {

        Files.walk(path, FileVisitOption.FOLLOW_LINKS)
                .map(Path::toFile)
                .forEach(file ->
                {
                    infoList.add(new FileInfoServer(Path.of(file.getAbsolutePath())));
                    System.out.println(new FileInfoServer(Path.of(file.getAbsolutePath())).toString());
                });
    }

    public List<FileInfoServer> getInfoList()
    {
        return infoList;
    }

    @Override
    public String toString()
    {
        return "PathInfo{" +
                "infoList=" + infoList +
                '}';
    }
}

