package ru.daniilazarnov;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 7777;
        String host = "127.0.0.1";
        RepoClient client = new RepoClient(host, port);
        client.start();
        Thread.sleep(500);
        client.deleteFile("test.txt");

        Thread.sleep(500);
        Path test = Paths.get("client\\src\\main\\java\\ru\\daniilazarnov\\test.txt");
        client.sendFile(test);

    }
}
