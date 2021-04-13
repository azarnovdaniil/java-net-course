package ru.daniilazarnov;

import ru.daniilazarnov.domain.FileMessage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    private Integer port;
    private String host;
    private String serverRepo;
    private String clientRepo;

    public Common() {
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getClientRepo() {
        return clientRepo;
    }

    public String getServerRepo() {
        return serverRepo;
    }

    public static Common readConfig() throws UnsupportedEncodingException, IOException {
        Common connect = new Common();
        InputStream is = Common.class.getClassLoader().getResourceAsStream("config.txt");
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader file = new BufferedReader(isr);
        String line = null; // line read from file
        while ((line = file.readLine()) != null) {
            String[]parametr = line.split(" - ");
            if (parametr[0].equals("port") && parametr[0] != null) {
                connect.port = Integer.valueOf(parametr[1]);
            } else if (parametr[0].equals("host") && parametr[0] != null) {
                connect.host = parametr[1];
            } else if (parametr[0].equals("server") && parametr[0] != null) {
                connect.serverRepo = parametr[1];
            } else if (parametr[0].equals("user") && parametr[0] != null) {
                connect.clientRepo = parametr[1];
            } else {
                System.out.println("Config file is fail");
            }
        }

        file.close(); isr.close(); is.close();
    return connect;
    }


    public FileMessage sendFile(String fileName, Path path) throws IOException {
        byte[] fileContent = Files.readAllBytes(path);
        return new FileMessage(fileName, fileContent);
    }

    public void receiveFile(Object msg, String directory) {
        try {
            Path path = Path.of(directory + "\\" + ((FileMessage) msg).getFileName());
            if (Files.exists(path)) {
                Files.delete(path);
            }
            Files.write(path, ((FileMessage) msg).getContent(), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StringBuilder showFiles(String path) {
        File dir = new File(path);
        StringBuilder sb = new StringBuilder();

        File[] files = dir.listFiles();
        sb.append(" For user: user " + "\n");
        for (File file : files) {
            long lastModified = file.lastModified();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            sb.append(file.getName() + " ,date of change " + sdf.format(new Date(lastModified)) + "\n");
        }
        return sb;
    }

    public String deleteFile(String address, String src) {
        address = src + "\\" + address;
        try {
            Files.delete(Path.of(address));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String("File was deleted");
    }

    public String renameFile(String lastName, String newName, String src) {
        String addressNew = src + "\\" + newName;
        if (Files.notExists(Path.of(addressNew))) {
            File file = new File(lastName);
            File newFile = new File(newName);
            file.renameTo(newFile);
            return new String("File was renamed");
        } else {
            return new String("File with name " + newName + "already exist");
        }
    }
}
