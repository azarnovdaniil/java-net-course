package ru.sviridovaleksey.workwithfiles;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;

public class ShowAllDirectory {

    private static final StringBuilder readyMessage = new StringBuilder();
    private static String defaultLink;

    public ShowAllDirectory(String defaultLink) {
        ShowAllDirectory.defaultLink = defaultLink;
    }


    public  StringBuilder startShowDirectory(String directoryAddress) {
        Path startingDir = Paths.get(directoryAddress);
        FileSearchExample crawler = new FileSearchExample();
        try {

            Files.walkFileTree(startingDir, Collections.singleton(FileVisitOption.FOLLOW_LINKS), 1 , crawler);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return readyMessage;
    }

    private static class FileSearchExample implements FileVisitor<Path> {
        private int count;

        public FileSearchExample() {
            readyMessage.setLength(0);
            readyMessage.append("//////////////////////////////////////////////////////////////////" + "\n");
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            readyMessage.append("Дирректория: ").append(StringUtils.substringAfter(dir.toString(),
                    Path.of(defaultLink).toString())).append("\n");
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            String fileName = file.getFileName().toString();
            count++;
            readyMessage.append(count).append(". ").append(fileName).append("\n");
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            readyMessage.append("такой папки не существует"  + "\n");
            readyMessage.append("//////////////////////////////////////////////////////////////////");
            return FileVisitResult.TERMINATE;

        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            readyMessage.append("//////////////////////////////////////////////////////////////////");
            return FileVisitResult.CONTINUE;
        }
    }


}
