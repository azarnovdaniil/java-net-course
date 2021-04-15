package ru.daniilazarnov;
import java.io.*;

public class Config {
    private Integer port;
    private String host;
    private String serverRepo;
    private String clientRepo;
    public static final String DEFAULT_CONFIG = "Config.txt";

    public Config() { }
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

    public static Config readConfig(String config) throws IOException {
        Config connect = new Config();
        InputStream is = Common.class.getClassLoader().getResourceAsStream(config);
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader file = new BufferedReader(isr);
        String line; // line read from file
        while ((line = file.readLine()) != null) {
            String[] parameter = line.split(" - ");
            if (parameter[0].equals("port") && parameter[1] != null && connect.port == null) {
                connect.port = Integer.valueOf(parameter[1]);
            }
            if (parameter[0].equals("host") && parameter[1] != null && connect.host == null) {
                connect.host = parameter[1];
            }
            if (parameter[0].equals("server") && parameter[1] != null && connect.serverRepo == null) {
                connect.serverRepo = parameter[1];
            }
            if (parameter[0].equals("user") && parameter[1] != null && connect.clientRepo == null) {
                connect.clientRepo = parameter[1];
            }
        }
        file.close(); isr.close(); is.close();
        if (connect.port != null && connect.host != null && connect.serverRepo != null && connect.clientRepo != null) {
            return connect;
        } else {
            return null;
        }
    }
}
