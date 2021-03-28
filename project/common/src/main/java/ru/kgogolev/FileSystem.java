package ru.kgogolev;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

public class FileSystem {


    public void walkFileTree(String path) throws IOException {
        Path currentPath  = Paths.get(path);
        Files.walkFileTree(currentPath, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (!dir.toFile().canRead()||dir.toFile().isHidden()){
                    return FileVisitResult.CONTINUE;
                } else if (dir.getNameCount()==currentPath.getNameCount()+1) {
                    System.out.println(String.format("subdir: %48s |",
                            dir.getName(dir.getNameCount() - 1)));
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                try {
                    if (!file.toFile().canRead() || file.toFile().isHidden()) {
                        return FileVisitResult.CONTINUE;
                    } else if (file.getNameCount() <= currentPath.getNameCount() + 1) {
                        System.out.println(String.format("file: %50s | %20s | %10d",
                                file.toFile().getName(),
                                new Date(file.toFile().lastModified()),
                                attrs.size()));
                    }
                } catch (Exception e){
                    return FileVisitResult.CONTINUE;
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
    public void walkAllFileTree(String path) throws IOException {
        Path currentPath  = Paths.get(path);
        Files.walkFileTree(currentPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println(dir);

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file.toFile().getName());
                return FileVisitResult.CONTINUE;
            }
        });
    }
}


